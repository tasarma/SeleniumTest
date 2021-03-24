package com.trendyol;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class BaseTest {
    WebDriver webDriver;

    @BeforeMethod
    public void startUp() throws InterruptedException {
        ChromeOptions options= new ChromeOptions();
        options.addArguments("--disable-notifications");

        System.setProperty("webdriver.chrome.driver", "C:/Users/Mahmut/Desktop/chromedriver.exe");
        webDriver = new ChromeDriver(options);

        webDriver.get("https://www.trendyol.com");

        webDriver.findElement(By.className("fancybox-close")).click();
        //Thread.sleep(2);

    }

    @Test
    public void login(){
        webDriver.findElement(By.className("account-user")).click();
        webDriver.findElement(By.id("login-email")).sendKeys("hz.hazret@gmail.com");
        webDriver.findElement(By.id("login-password-input")).sendKeys("xL3Z7CPAkZBjeqc");
        webDriver.findElement(By.className("submit")).click();

        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("component-list")));

        String accountButtonText = webDriver.findElement(By.className("account-user")).getText();
        Assert.assertEquals(accountButtonText, "Hesabım");

    }


    @AfterMethod
    public void testDown(){
        webDriver.quit();
    }
}
