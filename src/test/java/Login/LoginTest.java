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

public class LoginTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test
    void testLogin() {
        // mở trang login
        driver.get("http://localhost:5173/login");
        // nhập username
        WebElement usernameField = driver.findElement(By.id("username"));
        usernameField.sendKeys("admin");
        // nhập password
        WebElement passwordField = driver.findElement(By.id("password"));
        passwordField.sendKeys("admin");
        // nhấn nut1 đang nhập
        WebElement loginButton = driver.findElement(By.className("btn-primary"));
        loginButton.click();

        // kiểm tra dashboand
//        WebElement profileInfo = driver.findElement(By.xpath("//*[contains(text(),'Thông tin cá nhân')]"));
//        assertTrue(profileInfo.isDisplayed(),"Đăng nhap thất bại !!!");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement profileInfo = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("h3.card-header.bg-primary.text-white")
                )
        );

        assertTrue(profileInfo.isDisplayed(), "Đăng nhập thất bại !!!");

    }
}
