describe('Delete Club', () => {
    beforeEach(() => {
      cy.visit('/login')
      cy.get('#email').type('admin@example.com')
      cy.get('#password').type('password123')
      cy.get('button[type="submit"]').click()
      cy.contains('Clubs').click()
    })
  
    it('should handle deletion', () => {
      cy.intercept('DELETE', 'http://localhost:8080/clubs/*', {
        statusCode: 204
      }).as('deleteClub')
  
      cy.contains('Delete').first().click()
      cy.wait(500)
      cy.contains('Delete').first().click({ force: true })
    })
  })