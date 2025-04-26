package com.may.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.List;

public class LoginTest {

    private WebDriver driver;
    private WebDriverWait wait;

    // Khai báo biến username và password
    private String validUsername;
    private String validPassword;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup(); // Tự động lo vụ tải + setup
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Khởi tạo WebDriverWait ở đây

        // Gán giá trị mặc định cho validUsername và validPassword
        validUsername = "mobidoc"; // Đặt username mặc định của bạn
        validPassword = "1q2w3e4r!!"; // Đặt password mặc định của bạn
    }

    @BeforeMethod
    public void navigateToLoginPage() {
        driver.get("https://doctor-stag.mobidoc.at/login");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ✨ Hàm dùng chung để login với thao tác gõ ký tự và log quá trình
    private void login(String username, String password) {
        WebElement usernameField = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("___BVN__ID__v-3__input___")));
        WebElement passwordField = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("___BVN__ID__v-4__input___")));

        // Đảm bảo các trường nhập được làm sạch trước khi điền
        usernameField.clear();
        passwordField.clear();

        // Log các ký tự gõ vào username
        System.out.println("Đang gõ username: " + username);
        for (char c : username.toCharArray()) {
            usernameField.sendKeys(String.valueOf(c));
            try {
                Thread.sleep(100); // Thời gian giữa các ký tự để dễ nhìn thấy thao tác gõ
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Log các ký tự gõ vào password
        System.out.println("Đang gõ password: " + password);
        for (char c : password.toCharArray()) {
            passwordField.sendKeys(String.valueOf(c));
            try {
                Thread.sleep(100); // Thời gian giữa các ký tự để dễ nhìn thấy thao tác gõ
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'btn-content') and text()='Log in']")));
        loginBtn.click();
    }

//✅ Case 1: Login successfully
    @Test(priority = 1)
    public void testValidLogin() throws InterruptedException {
        login(validUsername, validPassword); // Sử dụng biến validUsername và validPassword

        // ✅ Chờ trang chuyển hướng thành công
        wait.until(ExpectedConditions.urlContains("/patient/consult"));

        // ✅ Optional: Chờ login button hiển thị và click được (phòng khi nút load chậm)
        WebElement loginButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("div.btn-content")));
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));

        // ✅ Xác thực URL chính xác
        Assert.assertTrue(driver.getCurrentUrl().contains("/patient/consult"), "✅ Login successfully!");
        Thread.sleep(100); // Delay 100ms

    }
//✅ Case 2: Login with wrong ID
    @Test(priority = 2)
    public void testInvalidUsername() throws InterruptedException {
        // Gọi login với username sai
        login("ABCABC", validPassword);

        // ✅ Chờ thông báo lỗi hiển thị
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Please check your ID and password')]")));

        // ✅ Xác thực rằng thông báo lỗi đang hiển thị
        Assert.assertTrue(errorMessage.isDisplayed(), "❌ Wrong ID");
        Thread.sleep(100); 
    }
// ✅ Case 3: Login with wrong Pass
    @Test(priority = 3)
    public void testInvalidPassword() throws InterruptedException {
        // Gọi login với mật khẩu sai
        login(validUsername, "wrongPass"); // Tự động nhập mật khẩu sai

        // ✅ Chờ thông báo lỗi hiển thị
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Please check your ID and password')]")));

        // ✅ Xác thực rằng thông báo lỗi đang hiển thị
        Assert.assertTrue(errorMessage.isDisplayed(), "❌ Wrong Password");
        Thread.sleep(100);
    }
//✅ Case 4:No fill ID
    @Test
    public void testEmptyUsername() throws InterruptedException {
        // ✅ Chờ trường mật khẩu hiển thị và nhập mật khẩu vào
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("___BVN__ID__v-4__input___")));
        passwordField.sendKeys(validPassword); // Tự động điền mật khẩu

        // ✅ Chờ nút Đăng nhập hiển thị
        WebElement loginButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("button.btn-custom-blue")));

        // ✅ Kiểm tra nếu nút Đăng nhập có class "disabled" và thuộc tính "disabled"
        boolean isLoginButtonDisabled = loginButton.getAttribute("disabled") != null ||
                loginButton.getAttribute("class").contains("disabled");
        Assert.assertTrue(isLoginButtonDisabled, "❌ No fill ID");
        Thread.sleep(100); 
    }
//✅ Case 5: No fill Password
    @Test
    public void testEmptyPassword() throws InterruptedException {
        WebElement usernameField = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("___BVN__ID__v-3__input___")));
        usernameField.sendKeys(validUsername);

        // ✅ Chờ nút Đăng nhập hiển thị
        WebElement loginButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("button.btn-custom-blue")));

        // ✅ Kiểm tra nếu nút Đăng nhập bị vô hiệu hóa (disabled)
        boolean isLoginButtonDisabled = loginButton.getAttribute("disabled") != null ||
                loginButton.getAttribute("class").contains("disabled");
        Assert.assertTrue(isLoginButtonDisabled, "❌ No fill password");
        Thread.sleep(100);
    }
//✅ Case 6: NonExistentUser
    @Test(priority = 6)
    public void testNonExistentUser() throws InterruptedException {
        // Gọi login với username không tồn tại
        login("nonExistentUser", validPassword); // Tự động nhập mật khẩu

        // ✅ Chờ thông báo lỗi hiển thị
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Please check your ID and password')]")));

        // ✅ Xác thực rằng thông báo lỗi đang hiển thị
        Assert.assertTrue(errorMessage.isDisplayed(), "❌ NonExistentUser");
        Thread.sleep(100);
    }
//✅ Case 7:Test button View password
    @Test(priority = 7)
    public void testViewPasswordToggle() throws InterruptedException {
        String testPassword = "Testviewpass";

        // ✅ Load lại trang login để đảm bảo sạch sẽ
        driver.get("https://doctor-stag.mobidoc.at/login");

        // ✅ Điền username
        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[type='text']")));
        usernameField.clear();
        usernameField.sendKeys("mobidocA");

        // ✅ Điền password
        WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[type='password']")));
        passwordField.clear();
        passwordField.sendKeys(testPassword);
        Thread.sleep(3000);

        // ✅ Click nút "View password"
        WebElement viewPasswordCheckbox = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("pwLook")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewPasswordCheckbox);

        // ✅ Chờ input password chuyển thành type='text' và có giá trị đúng
        WebElement revealedField = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(driver -> {
                    List<WebElement> inputs = driver.findElements(By.cssSelector("input[type='text']"));
                    for (WebElement input : inputs) {
                        if (testPassword.equals(input.getAttribute("value"))) {
                            return input;
                        }
                    }
                    return null;
                });
        // ✅ Kiểm tra kết quả
        Assert.assertEquals(revealedField.getAttribute("value"), testPassword,
                "❌ Mật khẩu không khớp – Test FAIL!");
        Thread.sleep(1000);
    }
//✅ Case 8: Test button Save login information
    /**
     * @throws InterruptedException
     */
    @Test(priority = 8)
    public void testSaveLoginInformation() throws InterruptedException {
        login(validUsername, validPassword); // Sử dụng biến validUsername và validPassword

        // ✅ Chờ trang chuyển hướng thành công
        wait.until(ExpectedConditions.urlContains("/patient/consult"));

        // Chờ trang chuyển tới dashboard sau khi đăng nhập
        wait.until(ExpectedConditions.urlContains("/patient/consult"));

        // Kiểm tra trang đã chuyển tới đúng
        Assert.assertTrue(driver.getCurrentUrl().contains("/patient/consult"), "❌ Đăng nhập thành công.");

        // Tìm và đảm bảo biểu tượng Mobicon SVG hiển thị trước khi click vào
        WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[contains(@class, 'btn-more')]")));
        Thread.sleep(1000);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        Thread.sleep(1000);
        button.click();

        // 👉 Chờ menu hoặc panel chứa các tùy chọn hiển thị ra
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'menu') or contains(@class, 'dropdown') or contains(., 'Log out')]")));

        // Tìm và click vào nút 'Log out'
        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(@class, 'logout-button') and contains(., 'Log out')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", logoutButton);
        Thread.sleep(1000);
        logoutButton.click();

        // Chờ chuyển hướng lại trang login
        wait.until(ExpectedConditions.urlContains("login"));
        Thread.sleep(1000);
    }
}