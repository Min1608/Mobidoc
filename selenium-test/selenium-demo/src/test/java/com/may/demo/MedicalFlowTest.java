package com.may.demo;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert; 
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

@SuppressWarnings("unused")
public class MedicalFlowTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private String validUsername;
    private String validPassword;
    private String validPreConsultation;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize(); // Mở toàn màn hình
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        validUsername = "mobidoc";
        validPassword = "1q2w3e4r!!";
        validPreConsultation = "Min test automation Medical Flow";
    }

    @BeforeMethod
    public void loginBeforeEachTest() {
        driver.get("https://doctor.mobidoc.at/login");
        login();
    }

    public void login() {
        try {
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("___BVN__ID__v-3__input___")));
            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("___BVN__ID__v-4__input___")));

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

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement registrationTab = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//ul[contains(@class, 'tabs-wrap')]//span[contains(text(), 'Registration')]")));
            registrationTab.click();

        } catch (Exception e) {
            System.err.println("Đăng nhập thất bại: " + e.getMessage());
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

/**
 * @throws InterruptedException
 */





// @Test(priority = 2)
// public void testClickPatientMinautoAndConfirmArrival() throws InterruptedException {
//     // Mở tab đã xác nhận
//     WebElement confirmedTab = wait.until(ExpectedConditions.elementToBeClickable(
//             By.cssSelector("label[for='common_consult_013']")));
//     confirmedTab.click();

//     // Chờ danh sách bệnh nhân load
//     wait.until(ExpectedConditions.elementToBeClickable(
//             By.xpath("//label[contains(@class, 'after-effect')]")));

//     // Tìm tất cả bệnh nhân tên Minauto
//     List<WebElement> patientElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
//             By.xpath("//span[contains(@class, 'patient-name') and contains(text(), 'Minauto')]")));
//     Assert.assertFalse(patientElements.isEmpty(), "❌ Không tìm thấy bệnh nhân tên Minauto.");

//     // Click bệnh nhân cuối cùng
//     WebElement lastPatient = patientElements.get(patientElements.size() - 1);
//     WebElement clickableArea = lastPatient.findElement(
//             By.xpath("./ancestor::div[contains(@class, 'bt-line')]"));
//     clickableArea.click();

//     // Đợi popup xác nhận hiện
//     wait.until(ExpectedConditions.visibilityOfElementLocated(
//             By.xpath("//p[contains(@class, 'alert-title') and contains(text(), 'Patient arrived?')]")));

//     // Tìm đúng nút "Confirm arrival"
//     WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(
//             By.xpath("//button[contains(@class, 'btn btn-md btn-custom-blue')]//div[text()=' Confirm arrival']")));

//     // Nếu bị che, click bằng JS
//     try {
//         confirmButton.click();
//     } catch (ElementClickInterceptedException e) {
//         ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmButton);
//     }

//     // Đợi popup biến mất hoàn toàn
//     wait.until(ExpectedConditions.invisibilityOfElementLocated(
//             By.xpath("//div[contains(@class, 'modal') and contains(@class, 'fade') and contains(@class, 'show')]")));

//     // Đảm bảo popup confirm không còn nữa
//     wait.until(ExpectedConditions.invisibilityOfElementLocated(
//             By.xpath("//p[contains(@class, 'alert-title') and contains(text(), 'Patient arrived?')]")));

//     // Xác nhận hoàn tất
//     Assert.assertTrue(true, "✅ Đã xác nhận arrival thành công.");
// }



// Case : Check Confirm reservation
    @Test
public void confirmReservation() {
    try {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement confirmedTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("label[for='common_consult_013']")));
        confirmedTab.click();

        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//label[contains(@class, 'after-effect')]")));

        List<WebElement> patientElements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//span[contains(@class, 'patient-name') and contains(text(), 'Minauto')]")));

        if (patientElements.isEmpty()) {
            System.err.println("Không tìm thấy bệnh nhân tên Minauto.");
            return;
        }

        WebElement lastPatientElement = patientElements.get(patientElements.size() - 1);
        WebElement clickableArea = lastPatientElement
                .findElement(By.xpath("./ancestor::div[contains(@class, 'bt-line')]"));
        clickableArea.click();

        WebElement confirmArrivalButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'btn-custom-blue')]//div[text()='Confirm arrival']")));
        confirmArrivalButton.click();

// 2. Lấy đúng thẻ <button> chứa Confirm arrival từ popup đầu tiên
System.out.println("Đang tìm popupConfirmButton.");
// 2. Lấy tất cả các thẻ <button> chứa "Confirm arrival"

try {
    WebElement exactConfirmButton = wait.until(ExpectedConditions.elementToBeClickable(
        By.xpath("//button[contains(@class, 'btn btn-md btn-custom-blue')]//div[text()=' Confirm arrival']")));
    exactConfirmButton.click();
    System.out.println("✅ Đã nhấn nút 'Confirm arrival' với class chính xác.");
} catch (Exception e ) {
  
        System.out.println("❌ Timeout: Không tìm thấy nút với class chính xác 'btn btn-md btn-custom-blue'.");
     
} 

// 4. Đợi popup đóng sau khi click
wait.until(ExpectedConditions.invisibilityOfElementLocated(
    By.xpath("//div[contains(@class, 'modal') and contains(@class, 'fade') and contains(@class, 'show')]"))
);

// 5. Đảm bảo tiêu đề cảnh báo cũng biến mất
wait.until(ExpectedConditions.invisibilityOfElementLocated(
    By.xpath("//p[contains(@class, 'alert-title') and contains(text(), 'Patient arrived?')]")));

        } catch (org.openqa.selenium.TimeoutException te) {
            System.err.println("❌ Timeout: Không tìm thấy phần tử trong thời gian chờ.");
        } catch (Exception e) {
            System.err.println("❌ Exception xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
    }

// Case : Check Accept Reservation
@Test
public void acceptReservation() {
    // Truy cập trang tư vấn
    driver.get("https://doctor.mobidoc.at/patient/consult");

    // Reload trang để đảm bảo dữ liệu được load đầy đủ
    driver.navigate().refresh();

    // Tìm tất cả các element chứa tên bệnh nhân Minauto
    List<WebElement> minaPatients = wait.until(
            ExpectedConditions.visibilityOfAllElementsLocatedBy(
                    By.xpath("//span[contains(@class, 'patient-name') and contains(text(), 'Minauto')]")));

    Assert.assertFalse(minaPatients.isEmpty(), "❌ Không tìm thấy bệnh nhân tên Minauto.");

    // Click vào dòng chứa bệnh nhân Minauto cuối cùng (trong trường hợp có nhiều dòng)
    WebElement lastMina = minaPatients.get(minaPatients.size() - 1);
    WebElement headerClickable = lastMina.findElement(By.xpath("./ancestor::div[contains(@class, 'bt-line')]"));
    headerClickable.click();

    // Đợi popup có chứa tiêu đề “Patient information” hiển thị
    WebElement popupTitle = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//p[contains(@class, 'modal-content') and contains(text(), 'Patient information')]")));

    // Xác nhận popup hiển thị
    Assert.assertTrue(popupTitle.isDisplayed(), "✅ Popup 'Patient information' đã hiển thị.");
}













    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
