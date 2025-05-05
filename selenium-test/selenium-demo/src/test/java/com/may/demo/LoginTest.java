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

    // Declare username and password variables
    private String validUsername;
    private String validPassword;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Create WebDriverWait

        // Assign default values ​​to validUsername and validPassword
        validUsername = "mobidoc";
        validPassword = "1q2w3e4r!!";
    }

    @BeforeMethod
    public void navigateToLoginPage() {
        driver.get("https://doctor.mobidoc.at/login");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ✨ Common function for login with character typing and process logging
    private void login(String username, String password) {
        WebElement usernameField = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("___BVN__ID__v-3__input___")));
        WebElement passwordField = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("___BVN__ID__v-4__input___")));

        // Make sure input fields are cleaned before filling.
        usernameField.clear();
        passwordField.clear();

        // Log characters typed in username
        System.out.println("Đang gõ username: " + username);
        for (char c : username.toCharArray()) {
            usernameField.sendKeys(String.valueOf(c));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Log các ký tự gõ vào password
        System.out.println("Đang gõ password: " + password);
        for (char c : password.toCharArray()) {
            passwordField.sendKeys(String.valueOf(c));
            try {
                Thread.sleep(100);
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
        login(validUsername, validPassword); 

        // ✅ Wait for page to redirect successfully
        wait.until(ExpectedConditions.urlContains("/patient/consult"));

        // ✅ Optional: Wait for the login button to appear and be clickable (in case the button loads slowly)
        WebElement loginButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("div.btn-content")));
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));

        // ✅ Check URL
        Assert.assertTrue(driver.getCurrentUrl().contains("/patient/consult"), "✅ Login successfully!");
        Thread.sleep(100);

    }
//✅ Case 2: Login with wrong ID
    @Test(priority = 2)
    public void testInvalidUsername() throws InterruptedException {
        // Input wrong username
        login("ABCABC", validPassword);

        // ✅ Wait notify
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Please check your ID and password')]")));

        // ✅ Check wrong notify
        Assert.assertTrue(errorMessage.isDisplayed(), "❌ Wrong ID");
        Thread.sleep(100); 
    }
// ✅ Case 3: Login with wrong Pass
    @Test(priority = 3)
    public void testInvalidPassword() throws InterruptedException {
        // Input wrong Password
        login(validUsername, "wrongPass"); // autofill wrong pass

        // ✅ Wait show notify
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Please check your ID and password')]")));

        // ✅ Check show wrong notify
        Assert.assertTrue(errorMessage.isDisplayed(), "❌ Wrong Password");
        Thread.sleep(100);
    }
//✅ Case 4:No fill ID
    @Test
    public void testEmptyUsername() throws InterruptedException {
        // ✅ Wait for the password field to appear and enter the password.
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("___BVN__ID__v-4__input___")));
        passwordField.sendKeys(validPassword);

        // ✅ Wait change state of Login button
        WebElement loginButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("button.btn-custom-blue")));

        // ✅ Check if Login button has class "disabled" and attribute "disabled"
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

        // ✅ Wait show login button
        WebElement loginButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("button.btn-custom-blue")));

        // ✅ Check if the Login button is disabled
        boolean isLoginButtonDisabled = loginButton.getAttribute("disabled") != null ||
                loginButton.getAttribute("class").contains("disabled");
        Assert.assertTrue(isLoginButtonDisabled, "❌ No fill password");
        Thread.sleep(100);
    }
//✅ Case 6: NonExistentUser
    @Test(priority = 6)
    public void testNonExistentUser() throws InterruptedException {
        // Login with username that does not exist
        login("nonExistentUser", validPassword);

        // ✅ Wait error notify
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(),'Please check your ID and password')]")));

        // ✅ Check show notify
        Assert.assertTrue(errorMessage.isDisplayed(), "❌ NonExistentUser");
        Thread.sleep(100);
    }
//✅ Case 7:Test button View password
    @Test(priority = 7)
    public void testViewPasswordToggle() throws InterruptedException {
        String testPassword = "Testviewpass";

        // ✅ Reload page login
        driver.get("https://doctor.mobidoc.at/login");

        // ✅ Input username
        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[type='text']")));
        usernameField.clear();
        usernameField.sendKeys("mobidocA");

        // ✅ Input password
        WebElement passwordField = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[type='password']")));
        passwordField.clear();
        passwordField.sendKeys(testPassword);
        Thread.sleep(3000);

        // ✅ Click "View password" button
        WebElement viewPasswordCheckbox = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.id("pwLook")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", viewPasswordCheckbox);

        // ✅ Wait input password change to type='text' and have right value
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
        // ✅ Check show result
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
        login(validUsername, validPassword);

        // ✅ Wait for reload true page
        wait.until(ExpectedConditions.urlContains("/patient/consult"));

        // Wait for the page to redirect to dashboard after login
        wait.until(ExpectedConditions.urlContains("/patient/consult"));

        // Check the page is redirected correctly
        Assert.assertTrue(driver.getCurrentUrl().contains("/patient/consult"), "❌ Đăng nhập thành công.");

        // Find and make sure the Mobicon SVG icon is visible before clicking on it.
        WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[contains(@class, 'btn-more')]")));
        Thread.sleep(1000);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        Thread.sleep(1000);
        button.click();

        // 👉 Wait show menu
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'menu') or contains(@class, 'dropdown') or contains(., 'Log out')]")));

        // Click 'Log out'
        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(@class, 'logout-button') and contains(., 'Log out')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", logoutButton);
        Thread.sleep(1000);
        logoutButton.click();

        // Wait for reload login page
        wait.until(ExpectedConditions.urlContains("login"));
        Thread.sleep(1000);
    }
}