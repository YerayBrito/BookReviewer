describe('API Error Handling', () => {
  beforeEach(() => {
    cy.visit('/')
  })

  it('should handle API errors gracefully', () => {
    // Setup error interceptor
    cy.intercept('GET', '**/api/books', {
      statusCode: 500,
      body: { message: 'Internal server error' }
    }).as('getBooksError')
    
    cy.visit('/')
    
    // Wait for the error response
    cy.wait('@getBooksError')
    
    // Verify error message is displayed
    cy.contains('Internal server error').should('be.visible')
  })

  it('should handle network errors', () => {
    // Setup network error interceptor
    cy.intercept('GET', '**/api/books', {
      forceNetworkError: true
    }).as('networkError')
    
    cy.visit('/')
    
    // Wait for the network error
    cy.wait('@networkError')
    
    // Verify error handling
    cy.get('body').should('contain', 'Network error')
  })

  it('should handle review submission errors', () => {
    // Setup normal books interceptor and error for reviews
    cy.intercept('GET', '**/api/books', {
      statusCode: 200,
      body: [
        {
          id: 1,
          title: "The Lord of the Rings",
          author: "J.R.R. Tolkien",
          reviews: []
        }
      ]
    }).as('getBooks')
    
    cy.intercept('POST', '**/api/reviews', {
      statusCode: 400,
      body: { message: 'Invalid review data' }
    }).as('createReviewError')
    
    cy.visit('/')
    cy.wait('@getBooks')
    
    // Navigate to book detail and try to submit a review
    cy.get('[data-cy="view-book-btn"]').first().click()
    cy.contains('Write Review').click()
    
    // Fill and submit the form
    cy.get('[data-cy="rating-input"]').click()
    cy.get('[data-cy="comment-input"]').type('Test review')
    cy.contains('Submit Review').click()
    
    // Wait for the error response
    cy.wait('@createReviewError')
    
    // Verify error message
    cy.contains('Invalid review data').should('be.visible')
  })

  it('should handle successful API calls after errors', () => {
    // First, setup error interceptor
    cy.intercept('GET', '**/api/books', {
      statusCode: 500,
      body: { message: 'Internal server error' }
    }).as('getBooksError')
    
    cy.visit('/')
    cy.wait('@getBooksError')
    
    // Then, setup successful interceptor and reload
    cy.intercept('GET', '**/api/books', {
      statusCode: 200,
      body: [
        {
          id: 1,
          title: "The Lord of the Rings",
          author: "J.R.R. Tolkien",
          reviews: []
        },
        {
          id: 2,
          title: "1984",
          author: "George Orwell",
          reviews: []
        },
        {
          id: 3,
          title: "Pride and Prejudice",
          author: "Jane Austen",
          reviews: []
        }
      ]
    }).as('getBooks')
    
    cy.reload()
    cy.wait('@getBooks')
    
    // Verify books are now loaded
    cy.get('[data-cy="book-list"]').should('be.visible')
    cy.get('[data-cy="book-item"]').should('have.length', 3)
  })
}) 