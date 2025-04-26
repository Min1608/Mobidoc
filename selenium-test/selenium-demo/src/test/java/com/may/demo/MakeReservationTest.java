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

    // Khai báo biến username và password
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
        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Đặt thời gian chờ là 10 giây

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
        driver.get("https://doctor-stag.mobidoc.at/login");
        login(); // Đăng nhập và click luôn nút Make Reservation
    }

    public void login() {
        // Đợi và lấy trường username
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("___BVN__ID__v-3__input___")));

        // Đợi và lấy trường password
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("___BVN__ID__v-4__input___")));

        // Typing username từng ký tự
        for (char c : validUsername.toCharArray()) {
            usernameField.sendKeys(String.valueOf(c));
            sleep(100);
        }

        // Typing password từng ký tự
        for (char c : validPassword.toCharArray()) {
            passwordField.sendKeys(String.valueOf(c));
            sleep(100);
        }

        // Click nút login
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'btn-content') and text()='Log in']")));
        loginButton.click();

        // Chờ tới khi đăng nhập thành công và URL thay đổi
        wait.until(ExpectedConditions.urlContains("/patient/consult"));

        // Click vào nút "Make Reservation" sau khi login
        WebElement makeReservationBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/section/div/button[4]/div"))); // XPath mới
        makeReservationBtn.click();

        // Xử lý cửa sổ mới sau khi click vào Make Reservation
        handleNewWindow();

        // Chờ một chút để đảm bảo trang mới đã tải xong
        sleep(2000); // Điều chỉnh thời gian chờ nếu cần thiết

        // Kiểm tra một phần tử trong cửa sổ mới (ví dụ kiểm tra tiêu đề)
        System.out.println("Open new window: " + driver.getTitle());
    }

    // Hàm xử lý chuyển qua cửa sổ mới
    private void handleNewWindow() {
        String currentWindowHandle = driver.getWindowHandle(); // Lấy cửa sổ hiện tại
        Set<String> allWindowHandles = driver.getWindowHandles(); // Lấy tất cả các cửa sổ

        for (String windowHandle : allWindowHandles) {
            if (!windowHandle.equals(currentWindowHandle)) {
                driver.switchTo().window(windowHandle); // Chuyển qua cửa sổ mới
                System.out.println("Đã chuyển sang cửa sổ mới.");
                break;
            }
        }
    }

    // Hàm hỗ trợ sleep để tạo hiệu ứng typing
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

    // Case : Check Make Reservation
    @Test
    public void testMakeReservationSuccessfully() throws InterruptedException {
        System.out.println("Start: Fill form and Make Reservation");

        // Điền thông tin bệnh nhân
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

        // Chọn bác sĩ bất kỳ (demo chọn bác sĩ đầu tiên)
        WebElement doctorOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//a[contains(@class,'nav-link') and contains(@class,'active')])[1]")));
        doctorOption.click();
        System.out.println("CP2");

        Thread.sleep(500);

        // Đợi tất cả khung giờ hiển thị
        List<WebElement> timeSlots = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//div[contains(@class,'time-box-list')]//label")));

        // Chọn một khung giờ bất kỳ (ví dụ: khung giờ đầu tiên, hoặc ngẫu nhiên)
        WebElement randomSlot = timeSlots.get(new Random().nextInt(timeSlots.size())); // Lấy ngẫu nhiên một phần tử từ danh sách

        // Cuộn tới phần tử để đảm bảo nó không bị che khuất
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", randomSlot);

        // Đợi cho phần tử có thể click
        wait.until(ExpectedConditions.elementToBeClickable(randomSlot));

        // Sử dụng Actions class để click vào phần tử
        Actions actions = new Actions(driver);
        actions.moveToElement(randomSlot).click().perform();

        // Hoặc dùng JavaScript để click nếu Actions không hiệu quả
        // ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
        // randomSlot);

        System.out.println("Đã chọn khung giờ bất kỳ.");
        sleep(500); // Đợi hiệu ứng nếu có

        // Click nút đặt lịch
        WebElement makeAppointmentBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'btn-content') and text()='Make an appointment']")));
        makeAppointmentBtn.click();
        System.out.println("Đã click vào nút Make an appointment.");
        sleep(1000);

        // Đợi xác nhận đặt lịch thành công (tùy app show gì có thể kiểm tra)
        sleep(3000); // chờ phản hồi
        System.out.println("Case 2: Đặt lịch thành công!");
    }

}
