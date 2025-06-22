describe('Book Reviewer Application', () => {
  beforeEach(() => {
    // Setup all interceptors directly
    cy.intercept('GET', '**/api/books', {
      statusCode: 200,
      body: [
        {
          id: 1,
          title: "The Lord of the Rings",
          author: "J.R.R. Tolkien",
          reviews: [
            {
              id: 1,
              rating: 5,
              comment: "A masterpiece of fantasy literature",
              user: { id: 1, username: "john_doe", email: "john@example.com" }
            }
          ]
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

    cy.intercept('GET', '**/api/users/1', {
      statusCode: 200,
      body: {
        id: 1,
        username: "john_doe",
        email: "john@example.com"
      }
    }).as('getUserById')

    cy.intercept('GET', '**/api/reviews', {
      statusCode: 200,
      body: [
        {
          id: 1,
          rating: 5,
          comment: "A masterpiece of fantasy literature",
          user: { id: 1, username: "john_doe", email: "john@example.com" },
          book: { id: 1, title: "The Lord of the Rings", author: "J.R.R. Tolkien" },
          createdAt: "2024-01-15T10:30:00Z"
        }
      ]
    }).as('getUserReviews')

    cy.intercept('POST', '**/api/reviews', {
      statusCode: 201,
      body: {
        id: 4,
        rating: 4,
        comment: "Great book!",
        user: { id: 1, username: "john_doe", email: "john@example.com" },
        book: { id: 1, title: "The Lord of the Rings", author: "J.R.R. Tolkien" }
      }
    }).as('createReview')
    
    cy.visit('/')
  })

  describe('Home Page', () => {
    it('should display the welcome message', () => {
      cy.contains('Welcome to BookReviewer').should('be.visible')
      cy.contains('Discover and review your favorite books').should('be.visible')
    })

    it('should show the book catalog section', () => {
      cy.contains('Book Catalog').should('be.visible')
    })

    it('should display navigation options', () => {
      cy.contains('Home').should('be.visible')
      cy.contains('About').should('be.visible')
    })
  })

  describe('Book List', () => {
    it('should display available books', () => {
      cy.wait('@getBooks')
      cy.get('[data-cy="book-list"]').should('be.visible')
    })

    it('should show book details for each book', () => {
      cy.wait('@getBooks')
      cy.get('[data-cy="book-item"]').should('have.length.at.least', 1)
      cy.get('[data-cy="book-title"]').should('be.visible')
      cy.get('[data-cy="book-author"]').should('be.visible')
    })

    it('should allow viewing book details', () => {
      cy.wait('@getBooks')
      cy.get('[data-cy="view-book-btn"]').first().click()
      cy.get('[data-cy="book-detail"]').should('be.visible')
    })
  })

  describe('Book Details', () => {
    beforeEach(() => {
      cy.wait('@getBooks')
      cy.get('[data-cy="view-book-btn"]').first().click()
    })

    it('should display book information', () => {
      cy.get('[data-cy="book-title"]').should('be.visible')
      cy.get('[data-cy="book-author"]').should('be.visible')
    })

    it('should show reviews section', () => {
      cy.get('[data-cy="reviews-section"]').should('be.visible')
    })

    it('should have a button to write a review', () => {
      cy.contains('Write Review').should('be.visible')
    })

    it('should allow returning to the catalog', () => {
      cy.contains('Back to Catalog').should('be.visible')
      cy.contains('Back to Catalog').click()
      cy.get('[data-cy="book-list"]').should('be.visible')
    })
  })

  describe('Review Form', () => {
    beforeEach(() => {
      cy.wait('@getBooks')
      cy.get('[data-cy="view-book-btn"]').first().click()
      cy.contains('Write Review').click()
    })

    it('should display the review form', () => {
      cy.get('[data-cy="review-form"]').should('be.visible')
      cy.contains('Write Your Review').should('be.visible')
    })

    it('should have rating selection', () => {
      cy.get('[data-cy="rating-input"]').should('be.visible')
    })

    it('should have comment field', () => {
      cy.get('[data-cy="comment-input"]').should('be.visible')
      cy.get('[data-cy="comment-input"]').should('have.attr', 'placeholder')
    })

    it('should have submit and cancel buttons', () => {
      cy.contains('Submit Review').should('be.visible')
      cy.contains('Cancel').should('be.visible')
    })

    it('should submit a review successfully', () => {
      // Select a rating
      cy.get('[data-cy="rating-input"]').click()
      
      // Add a comment
      cy.get('[data-cy="comment-input"]').type('This is a great book!')
      
      // Submit the review
      cy.contains('Submit Review').click()
      
      // Wait for the API call
      cy.wait('@createReview')
      
      // Verify the form is closed
      cy.get('[data-cy="review-form"]').should('not.exist')
    })
  })

  describe('User Profile', () => {
    beforeEach(() => {
      cy.wait('@getBooks')
    })

    it('should be accessible from navigation', () => {
      cy.get('[data-cy="user-profile-link"]').click()
      cy.wait('@getUserById')
      cy.contains('My Profile').should('be.visible')
    })

    it('should display user information', () => {
      cy.get('[data-cy="user-profile-link"]').click()
      cy.wait('@getUserById')
      cy.get('[data-cy="user-username"]').should('be.visible')
      cy.get('[data-cy="user-email"]').should('be.visible')
    })

    it('should show user statistics', () => {
      cy.get('[data-cy="user-profile-link"]').click()
      cy.wait(['@getUserById', '@getUserReviews'])
      cy.contains('My Statistics').should('be.visible')
      cy.contains('Reviews written').should('be.visible')
      cy.contains('Average rating').should('be.visible')
    })

    it('should display user reviews', () => {
      cy.get('[data-cy="user-profile-link"]').click()
      cy.wait(['@getUserById', '@getUserReviews'])
      cy.contains('My Reviews').should('be.visible')
    })
  })

  describe('Navigation', () => {
    beforeEach(() => {
      cy.wait('@getBooks')
    })

    it('should navigate between books and profile', () => {
      // Books view (default)
      cy.get('[data-cy="nav-books"]').should('have.class', 'bg-secondary')
      
      // Profile view
      cy.get('[data-cy="user-profile-link"]').click()
      cy.wait('@getUserById')
      cy.get('[data-cy="user-profile-link"]').should('have.class', 'bg-secondary')
      cy.contains('My Profile').should('be.visible')
      
      // Back to books
      cy.get('[data-cy="nav-books"]').click()
      cy.get('[data-cy="nav-books"]').should('have.class', 'bg-secondary')
    })

    it('should maintain navigation state', () => {
      cy.get('[data-cy="nav-menu"]').should('be.visible')
      cy.get('[data-cy="nav-books"]').should('have.class', 'bg-secondary')
    })
  })
}) 