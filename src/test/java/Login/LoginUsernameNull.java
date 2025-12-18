package Login;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginUsernameNull {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test
    void testLoginUsernameNull() {
        // mở trang login
        driver.get("http://localhost:5173/login");

        // nhập username
        WebElement usernameField = driver.findElement(By.id("username"));
        usernameField.sendKeys("");

        // nhập password
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("123456");

        // nhấn nut1 đang nhập
        WebElement loginButton = driver.findElement(By.className("btn-primary"));
        loginButton.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement usernameError = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(text(),'Vui lòng nhập username')]")
                )
        );
        assertTrue(usernameError.isDisplayed(),"Không hiển thị lỗi username rỗng! ");

        assertThrows(TimeoutException.class,() -> {
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'Vui lòng nhập mật khẩu')]")
            ));

        });
    }
}
