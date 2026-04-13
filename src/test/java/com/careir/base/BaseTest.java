package com.careir.base;

import com.careir.utils.ConfigReader;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {
    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        boolean headless = ConfigReader.getBoolean("headless");
        DriverFactory.initDriver(headless);
        WebDriver driver = DriverFactory.getDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        LOGGER.info("Driver initialized. Headless: {}", headless);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
        LOGGER.info("Driver closed.");
    }

    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }
}
