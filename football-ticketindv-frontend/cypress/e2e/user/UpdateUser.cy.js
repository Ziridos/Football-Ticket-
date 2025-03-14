describe('Update User Page', () => {
  beforeEach(() => {
    cy.visit('/login')
    cy.get('#email').type('admin@example.com')
    cy.get('#password').type('password123')
    cy.get('button[type="submit"]').click()
    
    cy.intercept('GET', 'http://localhost:8080/users/*', {
      statusCode: 200,
      body: {
        name: 'Test User',
        email: 'test@example.com',
        phone: '1234567890',
        address: '123 Test St',
        country: 'Test Country',
        city: 'Test City',
        postalCode: '12345',
        role: 'USER'
      }
    }).as('getUser')
    
    cy.contains('Users').click()
    cy.contains('Edit').first().click()
  })

  it('should display update user form with existing data', () => {
    cy.wait('@getUser')
    cy.get('input[name="name"]').should('have.value', 'Test User')
    cy.get('input[name="email"]').should('have.value', 'test@example.com')
    cy.get('input[name="phone"]').should('have.value', '1234567890')
    cy.get('input[name="address"]').should('have.value', '123 Test St')
    cy.get('input[name="country"]').should('have.value', 'Test Country')
    cy.get('input[name="city"]').should('have.value', 'Test City')
    cy.get('input[name="postalCode"]').should('have.value', '12345')
    cy.get('select[name="role"]').should('have.value', 'USER')
  })

  it('should show validation errors for invalid input', () => {
    cy.wait('@getUser')
    cy.get('form').invoke('attr', 'novalidate', 'novalidate')
    
    cy.get('input[name="name"]').clear()
    cy.get('input[name="email"]').clear()
    cy.get('input[name="phone"]').clear()
    
    cy.get('button[type="submit"]').click()

    cy.get('input[name="name"]')
      .should('have.class', 'is-invalid')
      .parent()
      .should('contain.text', 'Name can only contain letters and spaces')

    cy.get('input[name="email"]')
      .should('have.class', 'is-invalid')
      .parent()
      .should('contain.text', 'Email is required')

    cy.get('input[name="phone"]')
      .should('have.class', 'is-invalid')
      .parent()
      .should('contain.text', 'Phone is required')
  })

  it('should handle successful update', () => {
    cy.wait('@getUser')
    cy.intercept('PUT', 'http://localhost:8080/users/*', {
      statusCode: 200,
      body: { message: 'User updated successfully' }
    }).as('updateUser')

    const updatedName = 'Updated User'
    const updatedEmail = 'updated@example.com'
    const updatedPhone = '9876543210'
    
    cy.get('input[name="name"]').clear().type(updatedName)
    cy.get('input[name="email"]').clear().type(updatedEmail)
    cy.get('input[name="phone"]').clear().type(updatedPhone)
    cy.get('input[name="address"]').clear().type('456 Updated St')
    cy.get('input[name="country"]').clear().type('Updated Country')
    cy.get('input[name="city"]').clear().type('Updated City')
    cy.get('input[name="postalCode"]').clear().type('54321')
    cy.get('input[name="password"]').clear().type('Password123')

    cy.get('button[type="submit"]').click()
    cy.wait('@updateUser')
    cy.url().should('include', '/users')
  })

  it('should handle update error', () => {
    cy.wait('@getUser')
    cy.intercept('PUT', 'http://localhost:8080/users/*', {
      statusCode: 409,
      body: { message: 'Email already in use' }
    }).as('updateUserError')

    cy.get('input[name="email"]').clear().type('existing@example.com')
    cy.get('input[name="password"]').clear().type('Password123')
    cy.get('button[type="submit"]').click()
    cy.wait('@updateUserError')
    cy.get('.alert-danger').should('be.visible')
  })
})