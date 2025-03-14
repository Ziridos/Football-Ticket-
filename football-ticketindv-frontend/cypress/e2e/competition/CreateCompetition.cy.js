describe('Create Competition Page', () => {
  beforeEach(() => {
    cy.visit('/login')
    cy.get('#email').type('admin@example.com')
    cy.get('#password').type('password123')
    cy.get('button[type="submit"]').click()
    
    cy.contains('Competitions').click()
    cy.contains('Create New Competition').click()
  })

  it('should display create competition form', () => {
    cy.get('input[name="competitionName"]')
      .should('exist')
      .should('have.attr', 'placeholder', 'Competition Name')
  })

  it('should show validation errors for invalid input', () => {
    cy.get('form').invoke('attr', 'novalidate', 'novalidate')
    cy.get('button[type="submit"]').click()

    cy.get('input[name="competitionName"]')
      .should('have.class', 'form-control')
      .should('have.class', 'is-invalid')
      .parent()
      .should('contain.text', 'Competition Name is required')
  })

  it('should handle successful submission', () => {
    cy.intercept('POST', 'http://localhost:8080/competitions', {
      statusCode: 201,
      body: { message: 'Competition created successfully' }
    }).as('createCompetition')

    cy.get('input[name="competitionName"]')
      .should('not.be.disabled')
      .type('Test Competition')

    cy.get('button[type="submit"]').click()
    cy.wait('@createCompetition')
    cy.url().should('include', '/competitions')
  })

  it('should handle creation error', () => {
    cy.intercept('POST', 'http://localhost:8080/competitions', {
      statusCode: 409,
      body: { message: 'Competition already exists' }
    }).as('createCompetitionError')

    cy.get('input[name="competitionName"]')
      .should('not.be.disabled')
      .type('Existing Competition')

    cy.get('button[type="submit"]').click()
    cy.wait('@createCompetitionError')
    cy.get('.alert-danger').should('be.visible')
  })
})