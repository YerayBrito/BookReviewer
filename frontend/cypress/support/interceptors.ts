// API Interceptors for Cypress tests
// This file contains all the mock responses for the backend API

// Books API mocks
const mockBooks = [
  {
    id: 1,
    title: "The Lord of the Rings",
    author: "J.R.R. Tolkien",
    reviews: [
      {
        id: 1,
        rating: 5,
        comment: "A masterpiece of fantasy literature",
        user: { id: 1, username: "john_doe", email: "john@example.com" },
        book: { id: 1, title: "The Lord of the Rings", author: "J.R.R. Tolkien" }
      },
      {
        id: 2,
        rating: 4,
        comment: "Epic adventure with rich world-building",
        user: { id: 2, username: "jane_doe", email: "jane@example.com" },
        book: { id: 1, title: "The Lord of the Rings", author: "J.R.R. Tolkien" }
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
    reviews: [
      {
        id: 3,
        rating: 4,
        comment: "A classic romance with witty dialogue",
        user: { id: 1, username: "john_doe", email: "john@example.com" },
        book: { id: 3, title: "Pride and Prejudice", author: "Jane Austen" }
      }
    ]
  }
]

const mockUser = {
  id: 1,
  username: "john_doe",
  email: "john@example.com"
}

const mockUserReviews = [
  {
    id: 1,
    rating: 5,
    comment: "A masterpiece of fantasy literature",
    user: mockUser,
    book: { id: 1, title: "The Lord of the Rings", author: "J.R.R. Tolkien" },
    createdAt: "2024-01-15T10:30:00Z"
  },
  {
    id: 3,
    rating: 4,
    comment: "A classic romance with witty dialogue",
    user: mockUser,
    book: { id: 3, title: "Pride and Prejudice", author: "Jane Austen" },
    createdAt: "2024-01-20T14:45:00Z"
  }
]

// Interceptor functions
export const interceptors = {
  // Books API
  'GET /api/books': () => {
    cy.intercept('GET', '**/api/books', {
      statusCode: 200,
      body: mockBooks
    }).as('getBooks')
  },

  'GET /api/books/:id': (id: number = 1) => {
    const book = mockBooks.find(b => b.id === id) || mockBooks[0]
    cy.intercept('GET', `**/api/books/${id}`, {
      statusCode: 200,
      body: book
    }).as('getBookById')
  },

  'POST /api/books': () => {
    cy.intercept('POST', '**/api/books', {
      statusCode: 201,
      body: {
        id: 4,
        title: "New Book",
        author: "New Author",
        reviews: []
      }
    }).as('createBook')
  },

  // Reviews API
  'POST /api/reviews': () => {
    cy.intercept('POST', '**/api/reviews', {
      statusCode: 201,
      body: {
        id: 4,
        rating: 4,
        comment: "Great book!",
        user: mockUser,
        book: { id: 1, title: "The Lord of the Rings", author: "J.R.R. Tolkien" }
      }
    }).as('createReview')
  },

  'GET /api/reviews': () => {
    cy.intercept('GET', '**/api/reviews', {
      statusCode: 200,
      body: mockUserReviews
    }).as('getUserReviews')
  },

  // Users API
  'GET /api/users': () => {
    cy.intercept('GET', '**/api/users', {
      statusCode: 200,
      body: [mockUser]
    }).as('getUsers')
  },

  'GET /api/users/:id': (id: number = 1) => {
    cy.intercept('GET', `**/api/users/${id}`, {
      statusCode: 200,
      body: mockUser
    }).as('getUserById')
  },

  'POST /api/users': () => {
    cy.intercept('POST', '**/api/users', {
      statusCode: 201,
      body: {
        id: 3,
        username: "new_user",
        email: "new@example.com"
      }
    }).as('createUser')
  }
}

// Convenience functions for common scenarios
export const setupInterceptors = {
  // Setup all interceptors for a complete app experience
  all: () => {
    interceptors['GET /api/books']()
    interceptors['GET /api/users']()
    interceptors['POST /api/reviews']()
  },

  // Setup interceptors for book list view
  bookList: () => {
    interceptors['GET /api/books']()
  },

  // Setup interceptors for book detail view
  bookDetail: (bookId: number = 1) => {
    interceptors['GET /api/books']()
    interceptors['GET /api/books/:id'](bookId)
    interceptors['POST /api/reviews']()
  },

  // Setup interceptors for user profile view
  userProfile: (userId: number = 1) => {
    interceptors['GET /api/users/:id'](userId)
    interceptors['GET /api/reviews']()
  },

  // Setup interceptors for review form
  reviewForm: () => {
    interceptors['GET /api/books']()
    interceptors['POST /api/reviews']()
  }
}

// Error scenarios
export const errorInterceptors = {
  'GET /api/books - error': () => {
    cy.intercept('GET', '**/api/books', {
      statusCode: 500,
      body: { message: 'Internal server error' }
    }).as('getBooksError')
  },

  'POST /api/reviews - error': () => {
    cy.intercept('POST', '**/api/reviews', {
      statusCode: 400,
      body: { message: 'Invalid review data' }
    }).as('createReviewError')
  },

  'Network error': () => {
    cy.intercept('GET', '**/api/books', {
      forceNetworkError: true
    }).as('networkError')
  }
} 