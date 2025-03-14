describe('Login Flow', () => {
  beforeEach(() => {
    cy.visit('/login')
    cy.clearCookie('jwt-token')
  })

  it('should display login form correctly', () => {
    cy.get('img[alt="Logo"]').should('be.visible')
    cy.contains('Login to Football Ticket System')
    cy.get('#email').should('be.visible')
    cy.get('#password').should('be.visible')
    cy.get('button[type="submit"]').contains('LOGIN')
  })

  it('should login successfully with valid credentials', () => {
    cy.intercept('POST', 'http://localhost:8080/tokens', {
      statusCode: 200,
      body: { email: 'test@example.com', role: 'USER' }
    }).as('loginRequest')
    
    cy.get('#email').type('test@example.com')
    cy.get('#password').type('password123')
    cy.get('button[type="submit"]').click()

    cy.wait('@loginRequest')
    cy.url().should('eq', 'http://localhost:5173/')
  })

  it('should show error message for invalid credentials', () => {
    cy.intercept('POST', 'http://localhost:8080/tokens', {
      statusCode: 400,
      body: {}
    }).as('failedLogin')

    cy.get('#email').type('wrong@example.com')
    cy.get('#password').type('wrongpassword')
    cy.get('button[type="submit"]').click()

    cy.get('.alert-danger').should('be.visible')
      .and('contain', 'Invalid email or password')
  })

  it('should disable form during submission', () => {
    cy.intercept('POST', 'http://localhost:8080/tokens', {
      statusCode: 200,
      delayMs: 1000,
      body: { email: 'test@example.com', role: 'USER' }
    }).as('delayedLogin')

    cy.get('#email').type('test@example.com')
    cy.get('#password').type('password123')
    cy.get('button[type="submit"]').click()

    cy.get('#email').should('be.disabled')
    cy.get('#password').should('be.disabled')
    cy.get('button[type="submit"]').should('be.disabled')
      .and('contain', 'Logging in...')
  })

  
})