// cypress/integration/auth.spec.js
describe('Test feature Login (UI)', () => {
  const validUsername = Cypress.env('ADMIN_USER');
  const validPassword = Cypress.env('ADMIN_PASS');

  beforeEach(() => {
    cy.visit('https://doctor-stag.mobidoc.at/login'); // Thay đổi URL nếu cần

    // Kiểm tra giá trị của validUsername và validPassword
    cy.log('Valid Username:', validUsername);
    cy.log('Valid Password:', validPassword);
    // Đảm bảo các giá trị không bị undefined
    expect(validUsername).to.not.be.undefined;
    expect(validPassword).to.not.be.undefined;
  });

  it("✅ Case Log In SuccessfullySuccessfully", () => {
    cy.get('#___BVN__ID__v-3__input___', { timeout: 10000 }).should('be.visible').type(Cypress.env('ADMIN_USER'), { delay: 200 });
    cy.get('#___BVN__ID__v-4__input___', { timeout: 10000 }).should('be.visible').type(Cypress.env('ADMIN_PASS'), { delay: 200 });

    cy.contains('Log in').click();

    cy.url().should('include', 'https://doctor-stag.mobidoc.at/patient/consult'); // Sửa lại URL chính xác
    // Kiểm tra URL sau khi đăng nhập thành công
    cy.wait(2000);
  });

  it("❌ Case fill wrong ID", () => {
    cy.get('#___BVN__ID__v-3__input___', { timeout: 10000 }).should('be.visible').type('ABCABC', { delay: 200 });
    cy.get('#___BVN__ID__v-4__input___', { timeout: 10000 }).should('be.visible').type(validPassword, { delay: 200 });

    cy.contains('Log in').click();

    cy.contains('Please check your ID and password').should('be.visible');
  });

  it("❌ Case fill wrong password", () => {
    cy.get('#___BVN__ID__v-3__input___', { timeout: 10000 }).should('be.visible').type(validUsername, { delay: 200 });
    cy.get('#___BVN__ID__v-4__input___', { timeout: 10000 }).should('be.visible').type('wrongPass', { delay: 200 });

    cy.contains('Log in').click();

    cy.contains('Please check your ID and password').should('be.visible');
  });

  it('❌ Case No fill ID', () => {
    cy.get('#___BVN__ID__v-4__input___', { timeout: 10000 }).should('be.visible').type(validPassword, { delay: 200 });
  
    // Kiểm tra nếu nút "Log in" bị disable
    cy.contains('Log in').should('have.attr', 'disabled');

  });
  
  it('❌ Case no fill password', () => {
    cy.get('#___BVN__ID__v-3__input___', { timeout: 10000 }).should('be.visible').type(validUsername, { delay: 200 });

    cy.contains('Log in').should('have.attr', 'disabled');
  });

  it('❌ Case fill nonExistentUser', () => {
    cy.get('#___BVN__ID__v-3__input___', { timeout: 10000 }).should('be.visible').type('nonExistentUser', { delay: 200 });
    cy.get('#___BVN__ID__v-4__input___', { timeout: 10000 }).should('be.visible').type(validPassword, { delay: 200 });

    cy.contains('Log in').click();

    cy.contains('Please check your ID and password').should('be.visible');
  });
});