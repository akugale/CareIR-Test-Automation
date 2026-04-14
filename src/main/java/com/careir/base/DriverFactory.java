package com.careir.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public final class DriverFactory {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static void initDriver(boolean headless) {
        if (DRIVER.get() == null) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-notifications");
            options.addArguments("--start-maximized");
            options.setAcceptInsecureCerts(true);
            options.addArguments("--ignore-certificate-errors");
            options.addArguments("--allow-insecure-localhost");
            if (headless) {
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
            }
            DRIVER.set(new ChromeDriver(options));
        }
    }

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void quitDriver() {
        WebDriver webDriver = DRIVER.get();
        if (webDriver != null) {
            webDriver.quit();
            DRIVER.remove();
        }
    }
}
