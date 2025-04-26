// Import thư viện dotenv để đọc file .env
require('dotenv').config();

const { defineConfig } = require('cypress');

module.exports = defineConfig({
  // Sử dụng biến từ file .env
  env: {
    ADMIN_USER: process.env.ADMIN_USER, // Lấy từ .env
    ADMIN_PASS: process.env.ADMIN_PASS, // Lấy từ .env
    SERVER_URL: process.env.SERVER_URL, // Lấy từ .env
  },

  e2e: {
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
  },
});
