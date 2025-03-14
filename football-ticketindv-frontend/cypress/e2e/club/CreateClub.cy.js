describe('Create Club Page', () => {
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
  
      cy.contains('Clubs').click()
      cy.contains('Create New Club').click()
    })
  
    it('should display create club form', () => {
      cy.get('input[name="clubName"]').should('exist')
      cy.get('select[name="stadiumId"]').should('exist')
      cy.get('input[type="file"]').should('exist')
    })
  
    it('should show validation errors for invalid input', () => {
      cy.get('form').invoke('attr', 'novalidate', 'novalidate')
      cy.get('button[type="submit"]').click()
  
      cy.get('input[name="clubName"]')
        .should('have.class', 'is-invalid')
        .parent()
        .should('contain.text', 'Club Name is required')
  
      cy.get('select[name="stadiumId"]')
        .should('have.class', 'is-invalid')
        .parent()
        .should('contain.text', 'Stadium selection is required')
    })
  
    it('should handle successful submission', () => {
      
      cy.intercept('POST', 'http://localhost:8080/clubs', {
        statusCode: 201,
        body: { clubId: 1, message: 'Club created successfully' }
      }).as('createClub')
  
      cy.intercept('POST', 'http://localhost:8080/clubs/1/logo', {
        statusCode: 200,
        body: { clubId: 1, message: 'Logo uploaded successfully' }
      }).as('uploadLogo')
  
      cy.get('input[name="clubName"]').type('Test Club')
      cy.get('select[name="stadiumId"]').select('1')
      
  
      cy.get('button[type="submit"]').click()
      cy.wait('@createClub')
      cy.url().should('include', '/clubs')
    })
  
    it('should handle creation error', () => {
      cy.intercept('POST', 'http://localhost:8080/clubs', {
        statusCode: 409,
        body: { message: 'Club already exists' }
      }).as('createClubError')
  
      cy.get('input[name="clubName"]').type('Existing Club')
      cy.get('select[name="stadiumId"]').select('1')
      cy.get('button[type="submit"]').click()
      cy.wait('@createClubError')
      cy.get('.alert-danger').should('be.visible')
    })
  })
  