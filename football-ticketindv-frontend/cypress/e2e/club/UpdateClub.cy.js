describe('Update Club Page', () => {
    beforeEach(() => {
      cy.visit('/login')
      cy.get('#email').type('admin@example.com')
      cy.get('#password').type('password123')
      cy.get('button[type="submit"]').click()
      
      cy.intercept('GET', 'http://localhost:8080/stadiums', {
        statusCode: 200,
        body: [
          { id: 1, stadiumName: 'Test Stadium', stadiumCity: 'Test City', stadiumCountry: 'Test Country' }
        ]
      }).as('getStadiums')
  
      cy.intercept('GET', 'http://localhost:8080/clubs/*', {
        statusCode: 200,
        body: {
          clubId: 1,
          clubName: 'Test Club',
          stadium: { id: 1 }
        }
      }).as('getClub')
  
      cy.contains('Clubs').click()
      cy.contains('Edit').first().click()
    })
  
    it('should display update form with existing data', () => {
      cy.get('input[name="clubName"]').should('have.value', 'Test Club')
      cy.get('select[name="stadiumId"]').should('have.value', '1')
    })
  
    it('should show validation errors for invalid input', () => {

      cy.wait(1000)
      
      cy.get('input[name="clubName"]')
        .should('not.be.disabled')
        .then($input => {
          cy.get('form').invoke('attr', 'novalidate', 'novalidate')
          cy.wrap($input).clear()
          cy.get('button[type="submit"]').click()
    
          cy.get('input[name="clubName"]')
            .should('have.class', 'is-invalid')
            .parent()
            .should('contain.text', 'Club Name is required')
        })
    })
  
    it('should handle successful update', () => {
      
      cy.intercept('PUT', 'http://localhost:8080/clubs/*', {
        statusCode: 200,
        body: { message: 'Club updated successfully' }
      }).as('updateClub')
  
      cy.intercept('POST', 'http://localhost:8080/clubs/*/logo', {
        statusCode: 200,
        body: { message: 'Logo uploaded successfully' }
      }).as('uploadLogo')

      cy.wait(1000)
  
      cy.get('input[name="clubName"]').clear().type('Updated Club')
      cy.get('select[name="stadiumId"]').select('1')
  
      cy.get('button[type="submit"]').click()
      cy.wait('@updateClub')
      cy.url().should('include', '/clubs')
    })
  
    it('should handle update error', () => {
      cy.intercept('PUT', 'http://localhost:8080/clubs/*', {
        statusCode: 409,
        body: { message: 'Club name already exists' }
      }).as('updateClubError')

      cy.wait(1000)
  
      cy.get('input[name="clubName"]').clear().type('Existing Club')
      cy.get('button[type="submit"]').click()
      cy.wait('@updateClubError')
      cy.get('.alert-danger').should('be.visible')
    })
  })