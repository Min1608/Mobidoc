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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));       
        // Bước 1: Chờ cho phần tử trở nên có thể nhấp và click vào nút
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(
                        "#app > div > div.wrapper > div > div:nth-child(2) > div > main > div.list-content-wrap > div > div > div > div.patient-info > div.btn-wrap.icon-btn-wrap > div.badge-btn.record-btn")));
        button.click();
        sleep(3000); 


        //Click dropdown ở màn hình Medical historyhistory
        WebDriverWait waitClick = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement targetElement = waitClick.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//*[@id='modal-container-body']/div/div/div[1]/div[2]/div[2]/div[2]/div[2]/div/div[1]")));
        targetElement.click();
        sleep(1000);



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
            By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/div[2]/div[2]/div/div[1]/div[5]/div/div/div[2]/div/div/div/div/textarea")
        ));

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
                    "arguments[0].value = 'MinMin1';" +
                            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));",
                    textarea);
        } else {
            System.out.println("Textarea không thể nhập liệu.");
        }
        WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Chờ nút hiện ra
        WebElement button1 = wait1.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/div[2]/div[2]/div/div[1]/div[5]/div/div/div[3]/div/div/button[2]")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button1);
        button1.click();


// Click vào nút thứ 2 trong danh sách
        if (iconBoxes.size() >= 2) {
            iconBoxes.get(1).click();
        } else {
            System.out.println("Không đủ phần tử để click vào nút thứ 2.");
        }
        sleep(3000);

        // Tìm textarea bằng XPath
        WebElement textarea1 = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/div[2]/div[2]/div/div[1]/div[5]/div/div/div[2]/div/div/div/div/div[2]/textarea")
        ));
        textarea1.click();
        textarea1.clear();
        textarea1.sendKeys("Giá trị bạn muốn nhập");
        //Bước 6: Chờ và click vào nút Save
        WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(15));
        // Chờ cho nút sẵn sàng để click
        WebElement button2 = wait2.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/div[2]/div[2]/div/div[1]/div[5]/div/div/div[3]/div/div/button[2]")));
        // Scroll xuống để đảm bảo nút không bị che
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button2);
        button2.click();

// Click vào nút thứ 4
        if (iconBoxes.size() >= 4) {
            iconBoxes.get(3).click();
        } else {
            System.out.println("Ít hơn 4 nút, không click được nút thứ 4.");
        }
        sleep(3000);

        // Tìm textarea bằng XPath mới
        WebElement textarea2 = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/div[2]/div[2]/div/div[1]/div[5]/div/div/div[2]/div/div/div/div/textarea")
        ));

        // Scroll để đảm bảo không bị che
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", textarea2);
        textarea2.click();
        textarea2.clear();
        textarea2.sendKeys("Dữ liệu muốn nhập");

        // Bước 6: Chờ và click vào nút Save
        WebDriverWait wait3 = new WebDriverWait(driver, Duration.ofSeconds(20));
        // Chờ nút hiện ra
        WebElement button3 = wait3.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/div[2]/div[2]/div/div[1]/div[5]/div/div/div[3]/div/div/button[2]")));
        // Scroll đến nếu cần
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button3);
        button3.click();

// Click vào nút thứ 5
        if (iconBoxes.size() >= 5) {
            iconBoxes.get(4).click();
        } else {
            System.out.println("Ít hơn 4 nút, không click được nút thứ 4.");
        }
        sleep(3000);

        // Tìm textarea bằng XPath mới
        WebElement textarea3 = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/div[2]/div[2]/div/div[1]/div[5]/div/div/div[2]/div/div/div/div/textarea")
        ));
        // Click cho nó focus
        textarea3.click();
        // Xóa nội dung cũ (nếu có)
        textarea3.clear();
        // Nhập nội dung mới
        textarea3.sendKeys("Dữ liệu muốn nhập");

        // Bước 6: Chờ và click vào nút Save
        WebDriverWait wait4 = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Chờ nút hiện ra
        WebElement button4 = wait4.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[2]/div[2]/div[2]/div[2]/div/div[1]/div[5]/div/div/div[3]/div/div/button[2]")
        ));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button4);
        button4.click();
// Click vào nút thứ 6
        if (iconBoxes.size() >= 6) {
            iconBoxes.get(5).click();
        } else {
            System.out.println("Ít hơn 4 nút, không click được nút thứ 6.");
        }
        sleep(3000);       
        WebDriverWait wait5 = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Chờ và tìm textarea theo xpath
        WebElement textarea5 = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[3]/div/div[2]/div[3]/div[1]/textarea")
        ));

        // Cuộn tới textarea (tránh bị che khuất)
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", textarea5);

        // Xóa nội dung cũ (nếu có)
        textarea5.clear();

        // Nhập nội dung mới
        textarea5.sendKeys("Thông tin mới muốn nhập");
        sleep(3000);

        WebDriverWait wait6 = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Đếm số lượng items ban đầu
        List<WebElement> items = wait6.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
            By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[3]/div/div[2]/div[5]/p")
        ));

        int itemCount = items.size();

        for (int i = 1; i <= itemCount; i++) {
            // Lấy lại từng element mỗi vòng, tránh stale element
            WebElement item = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[3]/div/div[2]/div[5]/div[" + i + "]/p[1]")
            ));

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", item);

            // Click 2 lần cách nhau nửa giây
            item.click();
            sleep(500);
            item.click();
            sleep(500);
            item.click();
            sleep(500);
            item.click();
            sleep(500);
        }
        WebDriverWait wait7 = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Chờ và lấy element
        WebElement item = wait7.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[3]/div/div[2]/div[6]/div[1]/p[1]")
        ));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", item);

        // Click 4 lần liên tiếp, cách nhau nửa giây (500ms)
        for (int i = 0; i < 4; i++) {
            item.click();
            sleep(500);
        }
        WebDriverWait wait8 = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Chờ ô input hiển thị và có thể click được
        WebElement inputField = wait8.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[3]/div/div[2]/div[6]/div[2]/div[1]/div[1]/table/tbody/tr[1]/td[4]/div/div/form/div/input")
        ));

        // Cuộn đến ô input (tránh bị che)
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", inputField);

        // Click vào ô và nhập số 20
        inputField.click();
        inputField.clear();
        inputField.sendKeys("20");

        WebDriverWait wait9 = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Chờ ô input thứ 2 hiển thị và có thể click được
        WebElement secondInput = wait9.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[17]/div/div/div[2]/div/div/div/div[1]/div[3]/div/div[2]/div[6]/div[2]/div[1]/div[1]/table/tbody/tr[2]/td[4]/div/div/form/div/input")
        ));

        // Cuộn đến ô input (nếu bị che)
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", secondInput);

        // Click và nhập giá trị
        secondInput.click();
        secondInput.clear();
        secondInput.sendKeys("20");
        WebDriverWait wait10 = new WebDriverWait(driver, Duration.ofSeconds(20));
        // Chờ button chứa text "Complete" hiển thị
        WebElement completeBtn = wait10.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[.//div[text()='Complete']]")
        ));

        // Cuộn tới nút
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", completeBtn);

        // Dùng JavaScript click (phòng trường hợp bị che)
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", completeBtn);


// Click dropdown ở màn hình Medical historyhistory
        WebDriverWait waitClick1 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement targetElement1 = waitClick1.until(ExpectedConditions.elementToBeClickable(
        By.xpath("//*[@id='modal-container-body']/div/div/div[1]/div[2]/div[2]/div[2]/div[2]/div/div[1]")));
        targetElement1.click();
        sleep(3000);
    }

// Test case: Test payment in consult list by link
        @Test
            void byLink() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        // Chờ nút xuất hiện và có thể click
        // WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
        //     By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/main/div[2]/div/div/div/div[2]/div[2]/div[2]/button")
        // ));
        // ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        // ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);

        WebDriverWait wait11 = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> buttons = wait11.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
            By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/main/div[2]/div/div/div/div[2]/div[2]/div[2]/button")
        ));

        for (WebElement button : buttons) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }

        // Click phương thức thanh toán
        WebDriverWait wait8 = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement button8 = wait8.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[12]/div/div/div[2]/div/div/div[1]/div[5]/div/div/div[1]")
        ));
        button8.click();

        WebElement secondButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[12]/div/div/div[3]/div/div/button[2]")
        ));
        secondButton.click();
    sleep(5000);       
    }

// Test case: Test payment in consult list by cash
        @Test
            void byCash() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        // Chờ nút xuất hiện và có thể click
        WebDriverWait wait9 = new WebDriverWait(driver, Duration.ofSeconds(10));
        List<WebElement> buttons = wait9.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
            By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/main/div[2]/div/div/div/div[2]/div[2]/div[2]/button")
        ));

        for (WebElement button : buttons) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
            // Nếu click làm thay đổi DOM, bạn có thể cần chờ hoặc xử lý lại danh sách
        }

        WebElement secondButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("/html/body/div[1]/div/div[1]/div/div[2]/div/div[12]/div/div/div[3]/div/div/button[2]")
        ));
        secondButton.click();
    sleep(5000);       
    }

}
