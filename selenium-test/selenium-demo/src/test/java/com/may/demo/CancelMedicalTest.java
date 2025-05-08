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

public class CancelMedicalTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private String validUsername;
    private String validPassword;
    private String validPreConsultation;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        // driver.manage().window().maximize(); //Open full screen
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

        } catch (Exception e) {
            System.err.println("Login Failed: " + e.getMessage());
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

    private void fail(String string) {
        throw new UnsupportedOperationException("Unimplemented method 'fail'");
    }

//Case1 : Cancel in tab Arrival
    @Test (priority = 1)
    public void cancelArrivalTab() {
        // Click tab "Arrival"
        WebElement ConsultationTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//ul[contains(@class, 'tabs-wrap')]//span[contains(text(), 'Arrival')]")));
        ConsultationTab.click();

        WebElement cancelledTab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@class='nav-link']//span[contains(text(),'Cancelled')]")));
        String cancelledTextBefore = cancelledTab.getText(); 
        int cancelledCountBefore = Integer.parseInt(cancelledTextBefore.replaceAll("[^0-9]", ""));
        System.out.println("Cancelled before send: " + cancelledCountBefore);

        // Click Cancel button
        WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'btn-outline-red-gray')]//div[text()='Cancel']")));
        cancelButton.click();

        // Click Reservation Cancel button
        WebElement reservationCancelButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'btn-custom-red')]//div[text()='Reservation Cancel']")));
        reservationCancelButton.click();

        // Click Send text button
        WebElement sendTextButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'btn-custom-blue')]//div[text()=' Send text']")));
        sendTextButton.click();
        sleep(3000);

       
        WebElement cancelledTabAfter = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@class='nav-link']//span[contains(text(),'Cancelled')]")));
        String cancelledTextAfter = cancelledTabAfter.getText(); 
        int cancelledCountAfter = Integer.parseInt(cancelledTextAfter.replaceAll("[^0-9]", ""));
        System.out.println("Cancelled after send: " + cancelledCountAfter);

    }

/**
     * @throws InterruptedException
     */

    //Case2 : Cancel in tab Consultation
    @Test (priority = 2)
    public void cancelConsulTab() {
        // Click tab "Consultation"
        WebElement ConsultationTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//ul[contains(@class, 'tabs-wrap')]//span[contains(text(), 'Consultation')]")));
        ConsultationTab.click();

        
        WebElement cancelledTab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@class='nav-link']//span[contains(text(),'Cancelled')]")));
        String cancelledTextBefore = cancelledTab.getText(); 
        int cancelledCountBefore = Integer.parseInt(cancelledTextBefore.replaceAll("[^0-9]", ""));
        System.out.println("Cancelled number before send: " + cancelledCountBefore);

        // Click Cancel button
        WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'btn-outline-red-gray')]//div[text()='Cancel']")));
        cancelButton.click();

        // Click Reservation Cancel button
        WebElement reservationCancelButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'btn-custom-red')]//div[text()='Accept Cancel']")));
        reservationCancelButton.click();

        // Click Send text button
        WebElement sendTextButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'btn-custom-blue')]//div[text()=' Send text']")));
        sendTextButton.click();
        sleep(3000);

        
        WebElement cancelledTabAfter = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@class='nav-link']//span[contains(text(),'Cancelled')]")));
        String cancelledTextAfter = cancelledTabAfter.getText(); // Ví dụ: "Cancelled 1"
        int cancelledCountAfter = Integer.parseInt(cancelledTextAfter.replaceAll("[^0-9]", ""));
        System.out.println("Cancelled number after send: " + cancelledCountAfter);
    }

/**
     * @throws InterruptedException
     */

    //Case3 : Cancel in tab Dr's Diagnosis
    @Test (priority = 3)
    public void cancelDiagnosisTab() {
        // Click tab "Diagnosis"
        WebElement ConsultationTab = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//ul[contains(@class, 'tabs-wrap')]//span[contains(text(), 'Diagnosis')]")));
        ConsultationTab.click();

        
        WebElement cancelledTab = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@class='nav-link']//span[contains(text(),'Cancelled')]")));
        String cancelledTextBefore = cancelledTab.getText(); 
        int cancelledCountBefore = Integer.parseInt(cancelledTextBefore.replaceAll("[^0-9]", ""));
        System.out.println("Cancelled number before send: " + cancelledCountBefore);

        // Click Cancel button
        WebElement cancelButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'btn-outline-red-gray')]//div[text()='Cancel']")));
        cancelButton.click();

        // Click Reservation Cancel button
        WebElement reservationCancelButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'btn-custom-red')]//div[text()='Accept Cancel']")));
        reservationCancelButton.click();

        // Click Send text button
        WebElement sendTextButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class, 'btn-custom-blue')]//div[text()=' Send text']")));
        sendTextButton.click();
        sleep(3000);

        
        WebElement cancelledTabAfter = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@class='nav-link']//span[contains(text(),'Cancelled')]")));
        String cancelledTextAfter = cancelledTabAfter.getText(); 
        int cancelledCountAfter = Integer.parseInt(cancelledTextAfter.replaceAll("[^0-9]", ""));
        System.out.println("Cancelled number after send: " + cancelledCountAfter);
    }

}
