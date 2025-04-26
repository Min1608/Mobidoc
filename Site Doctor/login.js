// Import các thư viện cần thiết
const { Builder, By, Key, until } = require('selenium-webdriver');
require('dotenv').config();  // Import dotenv và cấu hình để sử dụng các biến môi trường từ .env

// Đọc các giá trị từ file .env
const SERVER_URL = process.env.SERVER_URL; // URL cần truy cập
const ADMIN_USER = process.env.ADMIN_USER; // Tên đăng nhập
const ADMIN_PASS = process.env.ADMIN_PASS; // Mật khẩu

// Hàm đăng nhập
async function loginToSite() {
  let driver = await new Builder().forBrowser('chrome').build(); // Tạo một instance của trình duyệt Chrome

  try {
    // Truy cập trang đăng nhập
    await driver.get(SERVER_URL); // Dùng SERVER_URL từ .env

    // Nhập thông tin đăng nhập
    await driver.findElement(By.name('username')).sendKeys(ADMIN_USER); // Dùng ADMIN_USER từ .env
    await driver.findElement(By.name('password')).sendKeys(ADMIN_PASS, Key.RETURN); // Dùng ADMIN_PASS từ .env

    // Chờ đợi trang chuyển hướng sau khi đăng nhập thành công
    await driver.wait(until.urlIs(SERVER_URL + '/dashboard'), 10000); // Thay '/dashboard' nếu cần

    console.log("Đăng nhập thành công!");
  } catch (error) {
    console.error('Có lỗi xảy ra:', error);
  } finally {
    await driver.quit(); // Đóng trình duyệt sau khi hoàn thành
  }
}

// Gọi hàm đăng nhập
loginToSite();
