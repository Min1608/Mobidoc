// Test/auth.test.js
const { login } = require('../Features/auth/auth.controller');

describe('Kiểm thử chức năng đăng nhập', () => {
  it("✅ Case Login Successfully", async () => {
// Giả định rằng hàm login trả về true khi đăng nhập thành công
    const result = await login(process.env.ADMIN_USER, process.env.ADMIN_PASS);
    expect(result).toBe(true);
  });

//Giả định rằng hàm login trả về false khi đăng nhập thất bại
 it("❌ Case Login fail (wrong Id)", async () => {
   const result = await login("saiUser", process.env.ADMIN_PASS);
   expect(result).toBe(false);
 });

 it("❌ Case Login fail (wrong Password)", async () => {
  const result = await login(process.env.ADMIN_USER, "wrongPass");
  expect(result).toBe(false);
});

// Thêm các trường hợp kiểm thử khác liên quan đến đăng nhập
 it('Case no handle id', async () => {
    const result = await login(null, process.env.ADMIN_PASS);
    expect(result).toBe(false);
 });

 it('Case no handle password', async () => {
  const result = await login(process.env.ADMIN_USER, null);
  expect(result).toBe(false);
});

 it('Case handle non-existent user', async () => {
    const result = await login("nonExistentUser", process.env.ADMIN_PASS);
    expect(result).toBe(false);
 });
});

