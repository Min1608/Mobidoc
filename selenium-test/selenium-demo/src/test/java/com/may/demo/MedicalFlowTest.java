package com.may.demo;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

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

                        // 👉 Clear input fields trước khi nhập
                        usernameField.clear();
                        passwordField.clear();

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

        // FORM DOCTOR MAKE RESERVATION

        // Case1 : Check Confirm reservation
        @Test(priority = 1)
        public void maReservationFlow() {
                try {
                        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                        WebElement confirmedTab = wait.until(ExpectedConditions.elementToBeClickable(
                                        By.cssSelector("label[for='common_consult_013']")));
                        confirmedTab.click();

                        wait.until(ExpectedConditions.elementToBeClickable(
                                        By.xpath("//label[contains(@class, 'after-effect')]")));

                        List<WebElement> patientElements = wait
                                        .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                                                        By.xpath(
                                                                        "//p[contains(@class, 'name')]//span[contains(@class, 'patient-name') and contains(text(), 'Minauto')]")));

                        if (patientElements.isEmpty()) {
                                System.err.println("Cannot file Minauto");
                                return;
                        }

                        WebElement lastPatientElement = patientElements.get(patientElements.size() - 1);
                        WebElement clickableArea = lastPatientElement
                                        .findElement(By.xpath("./ancestor::div[contains(@class, 'bt-line')]"));
                        clickableArea.click();

                        WebElement confirmArrivalButton = wait.until(ExpectedConditions.elementToBeClickable(
                                        By.xpath("//button[contains(@class, 'btn-custom-blue')]//div[text()='Confirm arrival']")));
                        confirmArrivalButton.click();

                        // Get the correct <button> tag containing Confirm arrival from the first popup
                        System.out.println("Đang tìm popupConfirmButton.");
                        // Get all <button> tags containing "Confirm arrival"

                        try {
                                WebElement exactConfirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                                                By.xpath(
                                                                "//button[contains(@class, 'btn btn-md btn-custom-blue')]//div[text()=' Confirm arrival']")));
                                exactConfirmButton.click();
                                System.out.println("✅ Clicked'Confirm arrival'");
                        } catch (Exception e) {

                                System.out.println("❌ Timeout: Cannot find class 'btn btn-md btn-custom-blue'.");

                        }

                        // Wait close popup after click
                        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                                        By.xpath(
                                                        "//div[contains(@class, 'modal') and contains(@class, 'fade') and contains(@class, 'show')]")));

                        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                                        By.xpath("//p[contains(@class, 'alert-title') and contains(text(), 'Patient arrived?')]")));

                } catch (org.openqa.selenium.TimeoutException te) {
                        System.err.println("❌ Timeout: Cannot find");
                } catch (Exception e) {
                        System.err.println("❌ Exception xảy ra: " + e.getMessage());
                        e.printStackTrace();
                }
                sleep(3000);
        }

        // Case2 : Check Consultation Reservation
        @Test(priority = 2)
        public void consultationReservation() {

                WebElement ConsultationTab = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//ul[contains(@class, 'tabs-wrap')]//span[contains(text(), 'Consultation')]")));
                ConsultationTab.click();

                // Find all elements containing patient name
                List<WebElement> minaPatients = wait.until(
                                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                                                By.xpath(
                                                                "//p[contains(@class, 'name')]//span[contains(@class, 'patient-name') and contains(text(), 'Minauto')]")));

                Assert.assertFalse(minaPatients.isEmpty(), "❌ Cannot find Minauto");

                WebElement lastMina = minaPatients.get(minaPatients.size() - 1);
                WebElement headerClickable = lastMina
                                .findElement(By.xpath("./ancestor::div[contains(@class, 'bt-line')]"));

                // Scroll vand click JS
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].scrollIntoView(true);", headerClickable);
                js.executeScript("arguments[0].click();", headerClickable);

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                // Wait open Patient information popup
                WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//div[contains(@class, 'modal-content')]//p[contains(text(), 'Patient information')]")));

                // Wait and click Accept button
                WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//div[contains(@class, 'modal-content')]//button[.//div[text()='Accept']]")));
                acceptButton.click();

                // Wait close popup (invisible)
                wait.until(ExpectedConditions.invisibilityOfElementLocated(
                                By.xpath("//div[contains(@class, 'modal-content')]")));

                // Wait and get the notification displayed after accept (e.g. alert success)
                WebElement alertMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.cssSelector(".alert, .toast, .notification")));

                System.out.println("Notify show: " + alertMessage.getText());
                sleep(3000);

        }

        // Case3 : Check Dr's Diagnosis 8 Reservation
        @Test(priority = 3)
        public void diagnosisReservation() {
                WebElement diagnosisTab = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//ul[contains(@class, 'tabs-wrap')]//span[contains(text(), 'Diagnosis')]")));
                diagnosisTab.click();

                // Find all elements containing patient name Minauto
                List<WebElement> minaPatients2 = wait.until(
                                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                                                By.xpath(
                                                                "//p[contains(@class, 'name')]//span[contains(@class, 'patient-name') and contains(text(), 'Minauto')]")));

                Assert.assertFalse(minaPatients2.isEmpty(), "❌ Không tìm thấy bệnh nhân tên Minauto.");

                // Choose lastMina2
                WebElement lastMina2 = minaPatients2.get(minaPatients2.size() - 1);

                // Find and click the "Complete" button in that line
                WebElement headerClickable2 = driver.findElement(By.xpath("//button//div[text()='Complete']"));
                headerClickable2.click();

                WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement endTreatmentButton = wait.until(ExpectedConditions
                                .elementToBeClickable(
                                                By.xpath("//div[@class='modal-footer']//button[contains(., 'End treatment')]")));
                endTreatmentButton.click();
                sleep(3000);

                // Wait to close popup
                wait.until(ExpectedConditions.invisibilityOfElementLocated(
                                By.xpath("//div[contains(@class, 'modal-content')]")));
                sleep(1000);
                // Click Completed Tab
                WebElement CompletedTab = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//ul[contains(@class, 'tabs-wrap')]//span[contains(text(), 'Completed')]")));
                CompletedTab.click();
                sleep(2000);
        }

        /**
         * @throws InterruptedException
         */

        // FORM PATIENT REGISTRATION (DOCTOR)
        @Test(priority = 4)
        public void PaRegistrationFlow() {
                WebElement ConsultationTab = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//ul[contains(@class, 'tabs-wrap')]//span[contains(text(), 'Consultation')]")));
                ConsultationTab.click();

                // wait to show information
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//*[@id='app']/div/div[1]/div/div[2]/div/main/div[2]/div/div/div")));

                // Find and Click Visit button with new Xpath
                WebElement visitButton = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//*[@id='app']/div/div[1]/div/div[2]/div/main/div[2]/div/div/div/div[2]")));
                visitButton.click();
                sleep(2000);

                // Create WebDriverWait
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

                // Find class <select> with XPath
                WebElement selectField = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[1]/div/div/div[2]/div/div/div/ul/li/div/div[2]/div[1]/div/select")));

                // Check show information of class select
                String selectedValue = selectField.getAttribute("value");
                if (selectedValue.isEmpty()) {
                        // If no show information -> open dropdown list and choose new item in list
                        selectField.click();

                        // Wait choose show full item in dropdown list
                        List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                                        By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[1]/div/div/div[2]/div/div/div/ul/li/div/div[2]/div[1]/div/select/option")));

                        // Check and choose item begin with "Dr"
                        WebElement selectedOption = null;
                        for (WebElement option : options) {
                                String optionText = option.getText();
                                if (optionText.startsWith("Dr.")) {
                                        selectedOption = option;
                                        break;
                                }
                        }

                        if (selectedOption != null) {
                                // Click item
                                selectedOption.click();

                                // Wait to close dropdown list
                                // Use JavascriptExecutor click outside to close dropdown list
                                JavascriptExecutor js = (JavascriptExecutor) driver;
                                js.executeScript("arguments[0].blur();", selectField);

                                // Wait to select value before close dropdown list
                                wait.until(ExpectedConditions.attributeToBe(selectField, "value",
                                                selectedOption.getAttribute("value")));

                                // Check choose item and print console
                                System.out.println("Giá trị đã chọn là: " + selectField.getAttribute("value"));
                        } else {
                                System.out.println("Không tìm thấy option bắt đầu bằng 'Dr.'");
                        }
                }
                sleep(2000);

                // Click Accept button
                WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//button[contains(@class, 'btn-custom-blue') and contains(., 'Accept')]")));
                acceptButton.click();
                sleep(2000);

                WebElement diagnosisTab = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//ul[contains(@class, 'tabs-wrap')]//span[contains(text(), 'Diagnosis')]")));
                diagnosisTab.click();
                sleep(2000);

                // Wait to show information
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//*[@id='app']/div/div[1]/div/div[2]/div/main/div[2]/div/div/div")));

                // Find and click the "Complete" button in that line
                WebElement headerClickable2 = driver.findElement(By.xpath("//button//div[text()='Complete']"));
                headerClickable2.click();

                WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement endTreatmentButton = wait.until(ExpectedConditions
                                .elementToBeClickable(
                                                By.xpath("//div[@class='modal-footer']//button[contains(., 'End treatment')]")));
                endTreatmentButton.click();
                sleep(2000);

                // Wait to close popup
                wait.until(ExpectedConditions.invisibilityOfElementLocated(
                                By.xpath("//div[contains(@class, 'modal-content')]")));
                sleep(1000);
                // Click Completed Tab
                WebElement CompletedTab = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//ul[contains(@class, 'tabs-wrap')]//span[contains(text(), 'Completed')]")));
                CompletedTab.click();
                sleep(2000);
        }

}
