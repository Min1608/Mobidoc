package com.may.demo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@SuppressWarnings("unused")
public class PatientRegistrationTest {

    private WebDriver driver;
    private WebDriverWait wait;

    // Declare username and password variables
    private String validUsername;
    private String validPassword;
    private String validName;
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
        validName = "MinPatientRe";
        // validPhone = "0908-776-3349";
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

        // Click Patient Registration button
        WebElement patientRegBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@id='app']/div/div[1]/div/div[2]/div/section/div/button[3]")));
        patientRegBtn.click();

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
                driver.switchTo().window(windowHandle); // Move to new window
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

    public static String generateRandomPhone() {
        Random rand = new Random();
        int part1 = rand.nextInt(90) + 10;       // 10–99
        int part2 = rand.nextInt(900) + 100;     // 100–999
        int part3 = rand.nextInt(9000) + 1000;   // 1000–9999
        return String.format("09%d-%03d-%04d", part1, part2, part3);
    }

    private int getAndIncrementCounter(String filename) {
        Path path = Paths.get(filename);
        int current = 1;
    
        try {
            if (Files.exists(path)) {
                String content = Files.readString(path).trim();
                if (!content.isEmpty()) {
                    current = Integer.parseInt(content);
                }
            }
            // Ghi lại giá trị đã tăng
            Files.writeString(path, String.valueOf(current + 1), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    
        return current;
    }

    // Case : Check Patient Registration (Doctor)
    @Test
    public void testPatientRegistrationSuccessfully() throws InterruptedException {
        System.out.println("Start: Fill form");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // // Input patient information
        // Tạo số điện thoại random theo format 09XX-XXX-XXXX
        String randomPhone = generateRandomPhone();

        // Nhập số vào input
        WebElement phoneInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[id^='__BVID__'][placeholder='Phone number']")));
        phoneInput.sendKeys(randomPhone);

        // Chờ nút Next có thể click được (loại bỏ trạng thái 'disabled')
        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement nextButton = wait2.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button.btn.btn-lg.btn-primary-fill.flex-grow-1:not(.disabled)")));
        nextButton.click();
        // Chờ chút xem có hiện gì không
        Thread.sleep(1000);
        System.out.println(driver.getPageSource());

        WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input.form-control[type='text']")));
        nameInput.sendKeys(validName);

// int counter = getAndIncrementCounter("name_counter.txt"); // dùng hàm có sẵn để lấy số tăng dần
// String validName = "MinPaRe" + counter; // gắn số đó vào tên
        
// WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
//         By.cssSelector("input.form-control[type='text']")));
// nameInput.sendKeys(validName);

        // Chờ nút Next có thể click được (loại bỏ trạng thái 'disabled')
        WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement next2Button = wait3.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button.btn.btn-x-lg.btn-primary-fill.flex-grow-1:not(.disabled)")));
        next2Button.click();

        WebElement birthInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input.form-control[type='date']")));
        birthInput.sendKeys(validBirth);

        // Tìm thẻ select và chọn giới tính tương ứng
        WebElement genderSelectElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("select.form-select.code-select-box")));

        Select genderSelect = new Select(genderSelectElement);

        if (validGender.equalsIgnoreCase("Female")) {
            genderSelect.selectByValue("F");
        } else {
            genderSelect.selectByValue("M");
        }

        // Chờ nút "Next" xuất hiện và có thể click được
        WebElement next33Button = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button.btn.btn-x-lg.btn-primary-fill.flex-grow-1")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", next33Button);
        Thread.sleep(1000);

        // Chờ nút "Find address" xuất hiện và có thể click được
        WebElement findAddressButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button.btn.btn-lg.btn-primary-fill.w-100")));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", findAddressButton);
        Thread.sleep(1000);


        // Tìm ô input địa chỉ và nhập "hcm"
        WebElement addressInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input#autocomplete.autocomplete-input")));
        addressInput.sendKeys("hcm");

        // Chờ gợi ý hiển thị (class phổ biến của Google Places: .pac-item)
        WebElement firstSuggestion = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".pac-item")));
        firstSuggestion.click();
        Thread.sleep(1000);

        // Chờ nút Confirm hiển thị và có thể click được
        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button.btn.btn-x-lg.btn-custom-blue")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", confirmButton);

        // Tạm dừng nếu cần theo dõi thao tác tiếp theo
        Thread.sleep(1000);

        // Chờ nút "Register a new patient" hiển thị và có thể click được
        WebElement registerNewPatientButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button.btn.btn-x-lg.btn-primary-fill.flex-grow-1")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", registerNewPatientButton);
        Thread.sleep(1000);

        WebDriverWait wait5 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement next4Button = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button.btn.btn-lg.btn-primary-fill.flex-grow-1")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", next4Button);
        Thread.sleep(1000);

        WebDriverWait wait6 = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Chờ field textarea sẵn sàng và nhập nội dung
        WebElement noteTextarea = wait6.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("textarea.form-control[maxlength='200']")));
        noteTextarea.clear();
        noteTextarea.sendKeys("Min check automation");

        // Lấy danh sách tất cả textarea có maxlength = 200
        List<WebElement> textareas = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
            By.cssSelector("textarea.form-control[maxlength='200']")));

        // Nhập vào textarea thứ 2
        if (textareas.size() >= 2) {
            WebElement secondTextarea = textareas.get(1);
            secondTextarea.clear();
            secondTextarea.sendKeys("Min check automation");
        } else {
            throw new RuntimeException("Không tìm thấy textarea thứ 2 để nhập nội dung.");
        }
        Thread.sleep(1000);


        WebDriverWait wait7 = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Lấy tất cả label có chữ "Yes"
        List<WebElement> yesLabels = wait7.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
            By.xpath("//label[.//span[contains(text(), 'Yes')]]")
        ));

        // Click từng label
        for (WebElement label : yesLabels) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", label);
            Thread.sleep(500); // Cho mượt
            label.click();
            Thread.sleep(500); // Tránh double click nhanh
        }

        WebElement next5Button = wait.until(ExpectedConditions.elementToBeClickable(
            By.cssSelector("button.btn.btn-lg.btn-primary-fill.flex-grow-1"))
        );

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", next5Button);
        Thread.sleep(1000); // Cho mượt sau khi click

        WebDriverWait wait8 = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Chờ và lấy ra label "Agree to all terms"
        WebElement checkboxDiv = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.form-check")));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", checkboxDiv);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", checkboxDiv);
        Thread.sleep(2000);

        WebElement confirm1Button = new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Confirm']")
            ));
        confirm1Button.click();
        Thread.sleep(2000);


        // 2. Chờ màn hình "Submission completed!" hiển thị
        boolean isSubmissionComplete = new WebDriverWait(driver, Duration.ofSeconds(15))
            .until(ExpectedConditions.textToBePresentInElementLocated(
                By.xpath("//p[contains(text(), 'Submission completed!')]"),
                "Submission completed!"
            ));

        // 3. Đánh giá kết quả
        if (isSubmissionComplete) {
            System.out.println("✅ Test Case PASSED: Submission completed message appeared.");
        } else {
            System.out.println("❌ Test Case FAILED: No completion message.");
        }

            }
}
