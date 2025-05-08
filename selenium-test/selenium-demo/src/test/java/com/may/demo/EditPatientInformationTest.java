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

public class EditPatientInformationTest {

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
    private String validAddress;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        validUsername = "mobidoc";
        validPassword = "1q2w3e4r!!";
        validName = "Min_autoEdit";
        validPhone = "0933-333-3333";
        validGender = "Male";
        validBirth = "08/16/1999";
        validEmail = "minhthu1608@dudaji.vn";
        validChartNumber = "33333333";
        validReservationNote = "Ear ,Nose, Mouth, EyesEdit";
        validEmName = "MinSisterEdit";
        validEmPhone = "0933-333-3333";
        validRelation = "SisterEdit";
        validAddress = "HCM";

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
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("___BVN__ID__v-3__input___")));

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("___BVN__ID__v-4__input___")));

        // 👉 Clear input fields
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

    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void fail(String string) {
        throw new UnsupportedOperationException("Unimplemented method 'fail'");
    }

    // Case : Edit patient information
    @Test(priority = 1)
    public void EditInformation() {

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
            System.err.println("Cannot find Minauto");
            return;
        }

        //Click patient to open popuppopup
        WebElement lastPatientElement = patientElements.get(patientElements.size() - 1);
        WebElement clickableArea = lastPatientElement
                .findElement(By.xpath("./ancestor::div[contains(@class, 'bt-line')]"));
        clickableArea.click();

        // Wait show popup
        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".edit-btn")));

        // Find and click edit button
        WebElement editButton = driver.findElement(By.cssSelector(".badge-btn.edit-btn"));
        editButton.click();

        // Use JS when cannot click edit button in form
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", editButton);
        sleep(1000);

        WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Clear and change value
        WebElement inputFieldName = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='___BVN__ID__v-85__input___']")));
        inputFieldName.click();
        inputFieldName.clear();
        inputFieldName.sendKeys(validName);

        WebElement inputFieldPhone = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='___BVN__ID__v-86__input___']")));
        inputFieldPhone.click();
        inputFieldPhone.clear();
        inputFieldPhone.sendKeys(validPhone);

        WebDriverWait wait4 = new WebDriverWait(driver, Duration.ofSeconds(10));

        if (validGender.equalsIgnoreCase("Female")) {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='M']"))).click();
        } else {
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='F']"))).click();
        }
        WebElement inputFieldBirth = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='___BVN__ID__v-91__input___']")));
        inputFieldBirth.click();
        inputFieldBirth.clear();
        inputFieldBirth.sendKeys(validBirth);

        WebElement inputFieldEmail = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='___BVN__ID__v-92__input___']")));
        inputFieldEmail.click();
        inputFieldEmail.clear();
        inputFieldEmail.sendKeys(validEmail);

        WebElement inputFieldAddress = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='___BVN__ID__v-93__input___']")));
        inputFieldAddress.click();
        inputFieldAddress.clear();
        inputFieldAddress.sendKeys(validAddress);

        WebElement inputFieldName2 = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='___BVN__ID__v-95__input___']")));
        inputFieldName2.click();
        inputFieldName2.clear();
        inputFieldName2.sendKeys(validEmName);

        WebElement inputFieldEmPhone = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='___BVN__ID__v-96__input___']")));
        inputFieldEmPhone.click();
        inputFieldEmPhone.clear();
        inputFieldEmPhone.sendKeys(validEmPhone);

        WebElement inputFieldEmRelation = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='___BVN__ID__v-97__input___']")));
        inputFieldEmRelation.click();
        inputFieldEmRelation.clear();
        inputFieldEmRelation.sendKeys(validRelation);

        // Click save button
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'btn-custom-blue')]//div[text()='Save']/parent::button")));
        saveButton.click();

        // wait to close popup edit patient information
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//input[@id='___BVN__ID__v-85__input___']")));
        sleep(3000);

    }

}
