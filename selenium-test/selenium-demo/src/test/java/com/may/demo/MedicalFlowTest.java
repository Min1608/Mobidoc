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
        // driver.manage().window().maximize(); // Mở toàn màn hình
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

// Case1 : Check Confirm reservation
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
                    By.xpath(
                            "//p[contains(@class, 'name')]//span[contains(@class, 'patient-name') and contains(text(), 'Minauto')]")));

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
                        By.xpath(
                                "//button[contains(@class, 'btn btn-md btn-custom-blue')]//div[text()=' Confirm arrival']")));
                exactConfirmButton.click();
                System.out.println("✅ Đã nhấn nút 'Confirm arrival' với class chính xác.");
            } catch (Exception e) {

                System.out.println("❌ Timeout: Không tìm thấy nút với class chính xác 'btn btn-md btn-custom-blue'.");

            }
            // 4. Đợi popup đóng sau khi click
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath(
                            "//div[contains(@class, 'modal') and contains(@class, 'fade') and contains(@class, 'show')]")));
            // 5. Đảm bảo tiêu đề cảnh báo cũng biến mất
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//p[contains(@class, 'alert-title') and contains(text(), 'Patient arrived?')]")));
        } catch (org.openqa.selenium.TimeoutException te) {
            System.err.println("❌ Timeout: Không tìm thấy phần tử trong thời gian chờ.");
        } catch (Exception e) {
            System.err.println("❌ Exception xảy ra: " + e.getMessage());
            e.printStackTrace();
        }
        sleep(3000);
        // }
// //Case2 : Check Consultation Reservation
        {// @Test(priority = 2)
         // public void consultationReservation() {

            WebElement ConsultationTab = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//ul[contains(@class, 'tabs-wrap')]//span[contains(text(), 'Consultation')]")));
            ConsultationTab.click();

            // Tìm tất cả các element chứa tên bệnh nhân Minauto
            List<WebElement> minaPatients = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(
                            By.xpath(
                                    "//p[contains(@class, 'name')]//span[contains(@class, 'patient-name') and contains(text(), 'Minauto')]")));

            Assert.assertFalse(minaPatients.isEmpty(), "❌ Không tìm thấy bệnh nhân tên Minauto.");

            // Click vào dòng chứa bệnh nhân Minauto cuối cùng (trong trường hợp có nhiều
            // dòng)
            WebElement lastMina = minaPatients.get(minaPatients.size() - 1);
            WebElement headerClickable = lastMina.findElement(By.xpath("./ancestor::div[contains(@class, 'bt-line')]"));
            headerClickable.click();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // 1. Đợi popup với tiêu đề "Patient information" hiển thị
            WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'modal-content')]//p[contains(text(), 'Patient information')]")));

            // 2. Đợi và click nút "Accept" trong popup
            WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class, 'modal-content')]//button[.//div[text()='Accept']]")));
            acceptButton.click();

            // 3. Đợi popup đóng (invisible)
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'modal-content')]")));

            // 4. Đợi và lấy thông báo hiển thị sau khi accept (ví dụ alert success)
            WebElement alertMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".alert, .toast, .notification")));

            System.out.println("Thông báo hiển thị: " + alertMessage.getText());
            sleep(3000);
        }
        // }

// //Case3 : Check Dr's Diagnosis 8 Reservation
        {// @Test
         // public void diagnosisReservation() {
            WebElement diagnosisTab = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//ul[contains(@class, 'tabs-wrap')]//span[contains(text(), 'Diagnosis')]")));
            diagnosisTab.click();

            // Tìm tất cả các element chứa tên bệnh nhân Minauto
            List<WebElement> minaPatients2 = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(
                            By.xpath(
                                    "//p[contains(@class, 'name')]//span[contains(@class, 'patient-name') and contains(text(), 'Minauto')]")));

            Assert.assertFalse(minaPatients2.isEmpty(), "❌ Không tìm thấy bệnh nhân tên Minauto.");

            // Lấy dòng cuối cùng chứa bệnh nhân Minauto
            WebElement lastMina2 = minaPatients2.get(minaPatients2.size() - 1);

            // Tìm và click vào nút "Complete" trong dòng đó
            WebElement headerClickable2 = driver.findElement(By.xpath("//button//div[text()='Complete']"));
            headerClickable2.click();

            WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement endTreatmentButton = wait.until(ExpectedConditions
                    .elementToBeClickable(
                            By.xpath("//div[@class='modal-footer']//button[contains(., 'End treatment')]")));
            endTreatmentButton.click();
            sleep(3000);

            // Đợi popup đóng (invisible)
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//div[contains(@class, 'modal-content')]")));
            sleep(3000);
            // Click Completed Tab
            WebElement CompletedTab = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//ul[contains(@class, 'tabs-wrap')]//span[contains(text(), 'Completed')]")));
            CompletedTab.click();
            sleep(3000);
        }
    }

}
