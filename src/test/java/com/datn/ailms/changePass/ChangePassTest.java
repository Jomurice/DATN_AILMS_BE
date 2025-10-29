package com.datn.ailms.changePass;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChangePassTest {
    private WebDriver webDriver;
    private final String baseUrl="http://localhost:5173/";

    @BeforeAll
    void setup(){
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUpTest(){
        webDriver = new ChromeDriver();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        webDriver.manage().window().maximize();
        webDriver.get(baseUrl);
    }

    @AfterEach
    void teardown(TestInfo testInfo){
        try {
            takeScreenshot(testInfo.getDisplayName());
        } catch (Exception e) {
            System.err.println("Không thể chụp ảnh: " + e.getMessage());
        } finally {
            if (webDriver != null) {
                webDriver.quit();
            }
        }
    }


    public void login(){
            webDriver.findElement(By.id("username")).sendKeys("admin");
            webDriver.findElement(By.id("password")).sendKeys("admin");
            webDriver.findElement(By.className("btn")).click();
    }

    @Test
    @DisplayName("testConfirmPassword")
    public void ConfirmPass(){
        try {
            Thread.sleep(3000);
            login();
            Thread.sleep(3000);
            webDriver.findElement(By.className("profile")).click();
            webDriver.findElement(By.id("changePass")).click();
            Thread.sleep(3000);
            webDriver.findElement(By.id("password")).sendKeys("admin");
            webDriver.findElement(By.id("newPassword")).sendKeys("123456");
            webDriver.findElement(By.id("confirmPassword")).sendKeys("123456");

            webDriver.findElement(By.className("btn-update")).click();
            Thread.sleep(3000);
            webDriver.findElement(By.className("profile")).click();
            Assertions.assertTrue(true);
        }catch (Exception e){
            e.printStackTrace();
            Assertions.fail("Lỗi trong test: " + e.getMessage());
        }
    }

    public void takeScreenshot(String name){
        if (webDriver == null) return;

        TakesScreenshot screenshot = (TakesScreenshot) webDriver;
        File srcFile =screenshot.getScreenshotAs(OutputType.FILE);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss"));

        String fileName ="screenshot/" + name + "_" +timestamp + ".png";

        try {
            File destFile = new File(fileName);
            destFile.getParentFile().mkdirs();
            Files.copy(srcFile.toPath(), destFile.toPath());
            System.out.println("Da chup hinh luu tai " + fileName);
        } catch (IOException e) {
            System.err.println("Không thể lưu ảnh chụp: " + e.getMessage());
        }
    }
}
