describe('App Loading', () => {
  beforeEach(() => {
    // Setup basic interceptors directly
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
    
    cy.visit('/')
  })

  it('should display the main application header', () => {
    cy.get('[data-cy="app-header"]').should('be.visible')
    cy.get('[data-cy="app-title"]').should('contain', 'Book Reviewer')
  })

  it('should show the navigation menu', () => {
    cy.get('[data-cy="nav-menu"]').should('be.visible')
    cy.get('[data-cy="nav-books"]').should('contain', 'Books')
    cy.get('[data-cy="user-profile-link"]').should('contain', 'My Profile')
  })

  it('should display the main content area', () => {
    cy.get('[data-cy="main-content"]').should('be.visible')
  })

  it('should load without errors', () => {
    cy.get('[data-cy="app-container"]').should('exist')
    cy.get('body').should('not.contain', 'Error')
  })

  it('should load books from API', () => {
    // Wait for the API call to complete
    cy.wait('@getBooks')
    
    // Verify books are displayed
    cy.get('[data-cy="book-list"]').should('be.visible')
    cy.get('[data-cy="book-item"]').should('have.length', 3)
  })
}) 