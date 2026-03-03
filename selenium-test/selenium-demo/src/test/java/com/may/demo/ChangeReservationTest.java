package com.may.demo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

public class ChangeReservationTest {
    private WebDriver driver;
    private WebDriverWait wait;

    // Declare username and password variables
    private String validUsername;
    private String validPassword;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize(); // Mở toàn màn hình
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
WebElement ConsultationTab = wait.until(ExpectedConditions.elementToBeClickable(
                                By.xpath("//ul[contains(@class, 'tabs-wrap')]//span[contains(text(), 'Registration')]")));
                ConsultationTab.click();

 sleep(5000);      
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));       
        // Bước 1: Chờ cho phần tử trở nên có thể nhấp và click vào nút
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(
                        "#app > div > div.wrapper > div > div:nth-child(2) > div > main > div.list-content-wrap > div > div > div > div.patient-info > div.btn-wrap.icon-btn-wrap > div.badge-btn.record-btn")));
        button.click();
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Tìm nút
        WebElement button1 = wait1.until(ExpectedConditions.presenceOfElementLocated(
            By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/div[2]/div[1]/div/div[1]/button/button")
        ));

        // Scroll tới nút
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button1);

        // Click bằng JS để bỏ qua overlay hoặc element bị chặn
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button1);

sleep(10000);


       // Wait to show full date&time
        List<WebElement> timeSlots = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//div[contains(@class,'time-box-list')]//label")));

        //Choose random time
        WebElement randomSlot = timeSlots.get(new Random().nextInt(timeSlots.size()));

        // Scroll time list
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", randomSlot);

        //Wait to click time
        wait.until(ExpectedConditions.elementToBeClickable(randomSlot));

        //Use Actions class to click on element
        Actions actions = new Actions(driver);
        actions.moveToElement(randomSlot).click().perform();

        System.out.println("Choosed random time");
        sleep(500);

        // Click Make an appointment
        WebElement makeAppointmentBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'btn-content') and text()='Make an appointment']")));
        makeAppointmentBtn.click();
        System.out.println("Choose Make an appointment.");
        sleep(1000);

        //Wait to Make reservation successfully
        sleep(3000); 
        System.out.println("Case: Make reservation successfully!");
  
    
}

 

        
            }




