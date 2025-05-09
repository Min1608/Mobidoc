package com.may.demo;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MedicalHistoryTest {
    private WebDriver driver;
    private WebDriverWait wait;

    // Declare username and password variables
    private String validUsername;
    private String validPassword;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        validUsername = "mobidoc";
        validPassword = "1q2w3e4r!!";
    }

    @BeforeMethod
    public void loginBeforeEachTest() {
        driver.get("https://doctor.mobidoc.at/login");
        login();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void login() {
        try {
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("___BVN__ID__v-3__input___")));
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("___BVN__ID__v-4__input___")));

            // Clear input fields trước khi nhập
            usernameField.clear();
            passwordField.clear();

            // Nhập username và password từng ký tự
            for (char c : validUsername.toCharArray()) {
                usernameField.sendKeys(String.valueOf(c));
                sleep(100);
            }

            for (char c : validPassword.toCharArray()) {
                passwordField.sendKeys(String.valueOf(c));
                sleep(100);
            }

            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class, 'btn-content') and text()='Log in']")));
            loginButton.click();

            wait.until(ExpectedConditions.urlContains("/patient/consult"));

            WebElement registrationTab = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//ul[contains(@class, 'tabs-wrap')]//span[contains(text(), 'Completed')]")));
            registrationTab.click();

        } catch (Exception e) {
            System.err.println("Login Failed " + e.getMessage());
            throw e;
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Test case: Test edit information of Medical History

    /**
     * @throws TimeoutException
     */
    @Test
    public void editMedicalHistory() throws TimeoutException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Bước 1: Chờ cho phần tử trở nên có thể nhấp và click vào nút
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(
                        "#app > div > div.wrapper > div > div:nth-child(2) > div > main > div.list-content-wrap > div > div > div > div.patient-info > div.btn-wrap.icon-btn-wrap > div.badge-btn.record-btn")));
        button.click();

        // Bước 2: Chờ một chút để trang load ổn định
        sleep(3000); // Hoặc có thể thay bằng một wait condition nếu cần

        // Tìm phần tử cha có class 'reserve-list reserve-tab'
        WebElement container = driver.findElement(By.cssSelector(".reserve-list.reserve-tab"));

        // Tìm tất cả các phần tử con có class 'icon-box after-effect' trong phần tử cha
        // đó
        List<WebElement> iconBoxes = container.findElements(By.cssSelector(".icon-box.after-effect"));

        // In ra số lượng phần tử
        System.out.println("Số lượng nút: " + iconBoxes.size());

        // Click vào nút đầu tiên trong danh sách
        if (!iconBoxes.isEmpty()) {
            iconBoxes.get(0).click();
        } else {
            System.out.println("Không tìm thấy nút nào để click.");
        }

        // Bước 4: Chờ popup xuất hiện và cuộn đến popup nếu cần
        WebElement popup = wait
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='modal-container-body']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", popup);

        // Bước 5: Chờ và click vào textarea để có thể nhập liệu
        WebElement textarea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(
                        "/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/div[2]/div[2]/div/div/div[14]/div/div/div[2]/div/div/div/div/textarea")));

        // Kiểm tra xem phần tử có thể nhấp được
        if (textarea.isDisplayed() && textarea.isEnabled()) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", textarea);
        } else {
            System.out.println("Textarea không thể tương tác.");
        }

        // Đảm bảo textarea đã có thể nhập liệu
        if (textarea.isEnabled()) {
            // Nhập giá trị vào textarea
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value = 'MinMin';" +
                            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                    textarea);
        } else {
            System.out.println("Textarea không thể nhập liệu.");
        }

        // Bước 6: Chờ và click vào nút Save
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                "/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/div[2]/div[2]/div/div/div[14]/div/div/div[3]/div/div/button[2]")));
        saveButton.click();

        // Bước 7: Chờ nút Save biến mất (hoặc toast xuất hiện nếu có) để xác nhận lưu
        // thành công
        wait.until(ExpectedConditions.invisibilityOf(saveButton));

        // Click vào nút thứ 2 trong danh sách
        if (iconBoxes.size() >= 2) {
            iconBoxes.get(1).click();
        } else {
            System.out.println("Không đủ phần tử để click vào nút thứ 2.");
        }
        sleep(3000);

        // Tìm textarea bằng XPath
        WebElement textarea1 = driver.findElement(By.xpath(
                "/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/div[2]/div[2]/div/div/div[14]/div/div/div[2]/div/div/div/div/div[2]/textarea"));

        // Click để focus
        textarea1.click();

        // Xóa nội dung cũ nếu cần
        textarea1.clear();

        // Nhập giá trị mới
        textarea1.sendKeys("Giá trị bạn muốn nhập1");

        // Bước 6: Chờ và click vào nút Save
        WebElement saveButton1 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                "/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/div[2]/div[2]/div/div/div[14]/div/div/div[3]/div/div/button[2]")));
        saveButton.click();

        // Bước 7: Chờ nút Save biến mất (hoặc toast xuất hiện nếu có) để xác nhận lưu
        // thành công
        wait.until(ExpectedConditions.invisibilityOf(saveButton1));
        sleep(3000);

        // Click vào nút thứ 4
        if (iconBoxes.size() >= 4) {
            iconBoxes.get(3).click();
        } else {
            System.out.println("Ít hơn 4 nút, không click được nút thứ 4.");
        }
        sleep(3000);

        // Tìm textarea bằng XPath mới
        WebElement textarea2 = driver.findElement(By.xpath(
                "/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/div[2]/div[2]/div/div/div[14]/div/div/div[2]/div/div/div/div/textarea"));

        // Click cho nó focus
        textarea2.click();

        // Xóa nội dung cũ (nếu có)
        textarea2.clear();

        // Nhập nội dung mới
        textarea2.sendKeys("Dữ liệu muốn nhập");

        // Bước 6: Chờ và click vào nút Save
        WebElement saveButton2 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                "/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/div[2]/div[2]/div/div/div[14]/div/div/div[3]/div/div/button[2]")));
        saveButton.click();

        // Bước 7: Chờ nút Save biến mất (hoặc toast xuất hiện nếu có) để xác nhận lưu
        // thành công
        wait.until(ExpectedConditions.invisibilityOf(saveButton2));
        sleep(3000);


// Click vào nút thứ 4
if (iconBoxes.size() >= 5) {
    iconBoxes.get(4).click();
} else {
    System.out.println("Ít hơn 4 nút, không click được nút thứ 4.");
}
sleep(3000);

// Tìm textarea bằng XPath mới
WebElement textarea3 = driver.findElement(By.xpath(
        "/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/div[2]/div[2]/div/div/div[14]/div/div/div[2]/div/div/div/div/textarea"));

// Click cho nó focus
textarea3.click();

// Xóa nội dung cũ (nếu có)
textarea3.clear();

// Nhập nội dung mới
textarea3.sendKeys("Dữ liệu muốn nhập");

// Bước 6: Chờ và click vào nút Save
WebElement saveButton3 = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
        "/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/div[2]/div[2]/div/div/div[14]/div/div/div[3]/div/div/button[2]")));
saveButton.click();

// Bước 7: Chờ nút Save biến mất (hoặc toast xuất hiện nếu có) để xác nhận lưu thành công
wait.until(ExpectedConditions.invisibilityOf(saveButton3));
sleep(3000);

// Click vào nút thứ 6
if (iconBoxes.size() >= 6) {
    iconBoxes.get(5).click();
} else {
    System.out.println("Ít hơn 4 nút, không click được nút thứ 6.");
}
sleep(3000);













    }

}
