package com.may.demo;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.Set;

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

public class MakeReservationTest {

    private WebDriver driver;
    private WebDriverWait wait;

    // Declare username and password variables
    private String validUsername;
    private String validPassword;
    private String validName;
    private String validPhone;
    private String validGender;
    private String validBirth;
    private String validEmail;
    private String validReservationNote;
    private String validEmPhone;
    private String validEmName;
    private String validRelation;
    private String validChartNumber;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        validUsername = "mobidoc";
        validPassword = "1q2w3e4r!!";
        validName = "Min_auto";
        validPhone = "0908-776-4449";
        validGender = "Female";
        validBirth = "08/16/2000";
        validEmail = "minhthu1608@dudaji.vn";
        validChartNumber = "98746573";
        validReservationNote = "Ear ,Nose, Mouth, Eyes";
        validEmName = "MinSister";
        validEmPhone = "0908-776-4449";
        validRelation = "Sister";

    }

    @BeforeMethod
    public void loginBeforeEachTest() {
        driver.get("https://doctor.mobidoc.at/login");
        login(); 
    }

    public void login() {
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("___BVN__ID__v-3__input___")));

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("___BVN__ID__v-4__input___")));

        // Typing username
        for (char c : validUsername.toCharArray()) {
            usernameField.sendKeys(String.valueOf(c));
            sleep(100);
        }
        // Typing password
        for (char c : validPassword.toCharArray()) {
            passwordField.sendKeys(String.valueOf(c));
            sleep(100);
        }

        // Click login
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'btn-content') and text()='Log in']")));
        loginButton.click();

        // Wait change page
        wait.until(ExpectedConditions.urlContains("/patient/consult"));

        // Click Make Reservation button
        WebElement makeReservationBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/section/div/button[4]/div")));
        makeReservationBtn.click();
        // Open new window
        handleNewWindow();
        sleep(2000);
        System.out.println("Open new window: " + driver.getTitle());
    }

    // Move to new window
    private void handleNewWindow() {
        String currentWindowHandle = driver.getWindowHandle(); 
        Set<String> allWindowHandles = driver.getWindowHandles(); 

        for (String windowHandle : allWindowHandles) {
            if (!windowHandle.equals(currentWindowHandle)) {
                driver.switchTo().window(windowHandle); //Move to new window
                System.out.println("Đã chuyển sang cửa sổ mới.");
                break;
            }
        }
    }

    // Sleep to typing
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

// Case : Check Make Reservation (Doctor)
    @Test
    public void testMakeReservationSuccessfully() throws InterruptedException {
        System.out.println("Start: Fill form and Make Reservation");

        // Input patient information
        driver.findElement(By.id("___BVN__ID__v-3__input___")).sendKeys(validName);
        driver.findElement(By.id("___BVN__ID__v-4__input___")).sendKeys(validPhone);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        if (validGender.equalsIgnoreCase("Female")) {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='F']"))).click();
        } else {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='M']"))).click();
        }
        System.out.println("CP1");

        driver.findElement(By.id("___BVN__ID__v-9__input___")).sendKeys(validBirth);
        driver.findElement(By.id("___BVN__ID__v-10__input___")).sendKeys(validEmail);
        driver.findElement(By.id("___BVN__ID__v-11__input___")).sendKeys(validChartNumber);
        driver.findElement(By.id("___BVN__ID__v-12__input___")).sendKeys("123 Lý Thường Kiệt, Quận 10");
        driver.findElement(By.id("___BVN__ID__v-13__input___")).sendKeys(validReservationNote);
        driver.findElement(By.id("___BVN__ID__v-14__input___")).sendKeys(validEmName);
        driver.findElement(By.id("___BVN__ID__v-15__input___")).sendKeys(validEmPhone);
        driver.findElement(By.id("___BVN__ID__v-16__input___")).sendKeys(validRelation);

        // Auto choose first doctor
        WebElement doctorOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//a[contains(@class,'nav-link') and contains(@class,'active')])[1]")));
        doctorOption.click();

        Thread.sleep(500);

        //Wait to show full date&time
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


        System.out.println("Đã chọn khung giờ bất kỳ.");
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
