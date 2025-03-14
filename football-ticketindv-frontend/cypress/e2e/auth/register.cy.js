describe('Register Page', () => {
  beforeEach(() => {
    cy.visit('/login')
    cy.contains('Register here').click()
  })
 
  it('should display registration form', () => {
    cy.get('input[name="name"]').should('exist')
    cy.get('input[name="email"]').should('exist')
    cy.get('input[name="password"]').should('exist')
    cy.get('input[name="phone"]').should('exist')
    cy.get('input[name="address"]').should('exist')
    cy.get('input[name="country"]').should('exist')
    cy.get('input[name="city"]').should('exist')
    cy.get('input[name="postalCode"]').should('exist')
  })
 
  it('should show validation errors for invalid input', () => {
    cy.get('input[name="name"]').type('123')
    cy.get('input[name="email"]').type('invalid-email') 
    cy.get('input[name="password"]').type('123') 
    cy.get('input[name="phone"]').type('123abc') 
    cy.get('input[name="address"]').type('$#@!')
    cy.get('input[name="country"]').type('123')
    cy.get('input[name="city"]').type('123')
    cy.get('input[name="postalCode"]').type('$#@!') 
  
    cy.get('button[type="submit"]').click()
    cy.get('.invalid-feedback').should('be.visible')
  })
  
  it('should register successfully with valid data', () => {
    cy.intercept('POST', 'http://localhost:8080/users/register', {
      statusCode: 201
    }).as('registerUser')
 
    cy.get('input[name="name"]').type('Test User')
    cy.get('input[name="email"]').type('test@example.com')
    cy.get('input[name="password"]').type('password123')
    cy.get('input[name="phone"]').type('1234567890')
    cy.get('input[name="address"]').type('123 Test St')
    cy.get('input[name="country"]').type('Test Country')
    cy.get('input[name="city"]').type('Test City')
    cy.get('input[name="postalCode"]').type('12345')
 
    cy.get('button[type="submit"]').click()
    cy.wait('@registerUser')
    cy.url().should('include', '/login')
  })
 
  
 
  it('should navigate back to login page', () => {
    cy.get('button.btn-outline-secondary').click()
    cy.url().should('include', '/login')
  })
 })