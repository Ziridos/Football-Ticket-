describe('Create User Page', () => {
  beforeEach(() => {
    cy.visit('/login')
    cy.get('#email').type('admin@example.com')
    cy.get('#password').type('password123')
    cy.get('button[type="submit"]').click()
    
    cy.contains('Users').click()
    cy.contains('Create New User').click()
  })
 
  it('should display create user form', () => {
    cy.get('input[name="name"]').should('exist')
    cy.get('input[name="email"]').should('exist')
    cy.get('input[name="password"]').should('exist')
    cy.get('input[name="phone"]').should('exist')
    cy.get('input[name="address"]').should('exist')
    cy.get('input[name="country"]').should('exist')
    cy.get('input[name="city"]').should('exist')
    cy.get('input[name="postalCode"]').should('exist')
    cy.get('select[name="role"]').should('exist')
  })
 
  it('should show validation errors for invalid input', () => {
    cy.get('form').invoke('attr', 'novalidate', 'novalidate')
    cy.get('button[type="submit"]').click()
  
    cy.get('input[name="name"]')
      .should('have.class', 'form-control')
      .should('have.class', 'is-invalid')
      .parent()
      .should('contain.text', 'Name can only contain letters and spaces')
  
    cy.get('input[name="email"]')
      .should('have.class', 'is-invalid')
      .parent()
      .should('contain.text', 'Email is required')
  
    cy.get('input[name="password"]')
      .should('have.class', 'is-invalid')
      .parent()
      .should('contain.text', 'Password should be at least 8 characters long')
  
    cy.get('input[name="phone"]')
      .should('have.class', 'is-invalid')
      .parent()
      .should('contain.text', 'Phone is required')
  })
 
  it('should handle successful submission', () => {
    cy.intercept('POST', 'http://localhost:8080/users', {
      statusCode: 201,
      body: { message: 'User created successfully' }
    }).as('createUser')

    cy.get('input[name="name"]').type('Test User')
    cy.get('input[name="email"]').type('test@example.com')
    cy.get('input[name="password"]').type('password123')
    cy.get('input[name="phone"]').type('1234567890')
    cy.get('input[name="address"]').type('123 Test St')
    cy.get('input[name="country"]').type('Test Country')
    cy.get('input[name="city"]').type('Test City')
    cy.get('input[name="postalCode"]').type('12345')
 
    cy.get('button[type="submit"]').click()
    cy.wait('@createUser')
    cy.url().should('include', '/users')
  })
 
  it('should handle creation error', () => {
    cy.intercept('POST', 'http://localhost:8080/users', {
      statusCode: 409,
      body: { message: 'User with this email already exists' }
    }).as('createUserError')

    cy.get('input[name="name"]').type('Test User')
    cy.get('input[name="email"]').type('existing@example.com')
    cy.get('input[name="password"]').type('password123')
    cy.get('input[name="phone"]').type('1234567890')
    cy.get('input[name="address"]').type('123 Test St')
    cy.get('input[name="country"]').type('Test Country')
    cy.get('input[name="city"]').type('Test City')
    cy.get('input[name="postalCode"]').type('12345')
 
    cy.get('button[type="submit"]').click()
    cy.wait('@createUserError')
    cy.get('.alert-danger').should('be.visible')
  })
})