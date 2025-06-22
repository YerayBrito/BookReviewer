// ***********************************************************
// This example support/index.js is processed and
// loaded automatically before your test files.
//
// This is a great place to put global configuration and
// behavior that modifies Cypress.
//
// You can change the location of this file or turn off
// automatically serving support files with the
// 'supportFile' configuration option.
//
// You can read more here:
// https://on.cypress.io/configuration
// ***********************************************************

// Import commands.js using ES2015 syntax:
import './commands'

// Import interceptors for direct use
import * as interceptors from './interceptors'

// Make interceptors available globally
Cypress.Commands.add('setupInterceptors', interceptors.setupAll)

// Type definitions for custom commands
declare global {
  namespace Cypress {
    interface Chainable {
      setupInterceptors: typeof interceptors.setupAll
      interceptors: typeof interceptors
    }
  }
}

// Alternatively, you can also use cy.intercept directly with the aliases:
// cy.wait('@getBooks')
// cy.wait('@createReview')
// etc.
