// Features/auth/auth.controller.js
require('dotenv').config();
const axios = require('axios');

async function login(id, passwd) {
  try {
    const response = await axios.post(`${process.env.SERVER_URL}/api/v1/login`, {
      id,
      passwd
    });

    // ⚠️ Kiểm tra đúng field trả về của hệ thống
    console.log("DATA :>> ",response.status);
    return response.data.jwtToken != null;
  } catch (error) {
    console.error("Login failed:", error.response?.data || error.message);
    return false;
  }
}
module.exports = { login };
