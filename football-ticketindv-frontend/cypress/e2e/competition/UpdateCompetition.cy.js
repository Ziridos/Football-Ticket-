describe('Update Competition Page', () => {
    beforeEach(() => {
      cy.visit('/login')
      cy.get('#email').type('admin@example.com')
      cy.get('#password').type('password123')
      cy.get('button[type="submit"]').click()
      
      cy.intercept('GET', 'http://localhost:8080/competitions/*', {
        statusCode: 200,
        body: {
          id: 1,
          competitionName: 'Test Competition'
        }
      }).as('getCompetition')
      
      cy.contains('Competitions').click()
      cy.contains('Edit').first().click()
    })
  
    it('should display update form with existing data', () => {
      cy.wait('@getCompetition')
      cy.get('input[name="competitionName"]')
        .should('exist')
        .should('have.value', 'Test Competition')
    })
  
    it('should show validation errors for invalid input', () => {
      cy.wait('@getCompetition')
      cy.get('form').invoke('attr', 'novalidate', 'novalidate')
      cy.get('input[name="competitionName"]').clear()
      cy.get('button[type="submit"]').click()
  
      cy.get('input[name="competitionName"]')
        .should('have.class', 'is-invalid')
        .parent()
        .should('contain.text', 'Competition Name is required')
    })
  
    it('should handle successful update', () => {
      cy.wait('@getCompetition')
      cy.intercept('PUT', 'http://localhost:8080/competitions/*', {
        statusCode: 200,
        body: { message: 'Competition updated successfully' }
      }).as('updateCompetition')
  
      cy.get('input[name="competitionName"]')
        .clear()
        .type('Updated Competition')
  
      cy.get('button[type="submit"]').click()
      cy.wait('@updateCompetition')
      cy.url().should('include', '/competitions')
    })
  
    it('should handle update error', () => {
      cy.wait('@getCompetition')
      cy.intercept('PUT', 'http://localhost:8080/competitions/*', {
        statusCode: 409,
        body: { message: 'Competition name already exists' }
      }).as('updateCompetitionError')
  
      cy.get('input[name="competitionName"]')
        .clear()
        .type('Existing Competition')
  
      cy.get('button[type="submit"]').click()
      cy.wait('@updateCompetitionError')
      cy.get('.alert-danger').should('be.visible')
    })
  })