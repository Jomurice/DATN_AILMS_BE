package Login;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTestFail {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test
    void testLoginFail() {
        // mở trang login
        driver.get("http://localhost:5173/login");

        // nhập username
        WebElement usernameField = driver.findElement(By.id("username"));
        usernameField.sendKeys("NV026");

        // nhập password
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("123456");

        // nhấn nut1 đang nhập
        WebElement loginButton = driver.findElement(By.className("btn-primary"));
        loginButton.click();

        // kiểm tra dashboand

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement errorMsg = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("div.alert.alert-danger.py-2")
                )
        );

        // kiểm tra thông báo lổi có hiển thị
        assertTrue(errorMsg.isDisplayed(),"Không hiển thi thông báo lỗi khi đăng nhập thất bại");

        // kiem tra noi dung chiính xác
        String actualMsg = errorMsg.getText();
        assertTrue(actualMsg.contains("Tên đăng nhập hoặc mật khẩu không đúng !"), "Thông báo không đúng! Thực tế: "+ actualMsg);

    }
}
