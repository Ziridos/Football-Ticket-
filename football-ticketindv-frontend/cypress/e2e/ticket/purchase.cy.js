Cypress.Commands.add('clickKonvaGroup', (stageSelector, boxName) => {
  cy.get(stageSelector).find('canvas').should('exist').then($canvas => {
    const stage = $canvas[0].getContext('2d').__canvas.stage;
    const group = stage.findOne(`#box-${boxName}`);
    
    if (!group) {
      throw new Error(`Box with name "${boxName}" not found`);
    }
    
    const box = group.getClientRect();
    cy.get(stageSelector).find('canvas')
      .trigger('mousedown', box.x + box.width / 2, box.y + box.height / 2);
    
    cy.wait(100);
    
    cy.get(stageSelector).find('canvas')
      .trigger('mouseup', box.x + box.width / 2, box.y + box.height / 2);
  });
});

Cypress.Commands.add('clickKonvaBox', (stageSelector) => {
  cy.get(stageSelector).find('canvas').should('exist').then($canvas => {
    cy.get(stageSelector).find('canvas')
      .trigger('mousedown', 100, 100);
    
    cy.wait(100);
    

  });
});

Cypress.Commands.add('clickKonvaSeat', (stageSelector, seatId) => {
  cy.get(stageSelector).find('canvas').should('exist').then($canvas => {
    cy.wait(500); 
    
    cy.get(stageSelector).find('canvas')
      .trigger('mousedown', 142, 31, { force: true });
    
    cy.wait(200); 
    
    cy.get(stageSelector).find('canvas')
      .trigger('mouseup', 142, 31, { force: true });
    
    cy.wait(200); 
  });
});

describe('Football Ticket Purchase Flow', () => {
  beforeEach(() => {
    cy.visit('/login');
    cy.get('#email').type('admin@example.com');
    cy.get('#password').type('password123');
    cy.get('button[type="submit"]').click();
    cy.url().should('not.include', '/login');
  });

  it('should complete entire ticket purchase flow', () => {
    cy.contains('PURCHASE TICKETS').click();
    cy.url().should('include', '/ticket-purchase');
    cy.contains('Select Seats').should('be.visible');
    
    cy.contains('button', 'Select Seats').first().click();
    cy.url().should('include', '/ticket-purchase/boxes');
    cy.contains('Select a Box').should('be.visible');
    
    const boxX = 114 + (104/2);
    const boxY = 37 + (62/2);
    
    cy.get('[data-testid="stadium-stage"]').find('canvas')
      .trigger('mousedown', boxX, boxY);
    
    cy.wait(100);
    
    cy.get('[data-testid="stadium-stage"]').find('canvas')
      .should('exist')
      .trigger('mouseup', boxX, boxY);
    
    cy.url().should('include', '/ticket-purchase/seats');
    
    cy.get('[data-testid="seating-stage"]').should('exist');
    cy.clickKonvaSeat('[data-testid="seating-stage"]');
    
    cy.contains('button', 'Proceed to Payment').should('be.visible').click();
    cy.wait(2000);
    cy.contains('button', 'Pay Now').first().should('be.visible').click();
  });

  it('should handle sold out matches', () => {
    cy.contains('PURCHASE TICKETS').click();
    cy.contains('button', 'Match Completed')
      .should('be.visible')
      .should('be.disabled');
  });

});