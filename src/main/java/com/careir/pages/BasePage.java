package com.careir.pages;

import com.careir.utils.ConfigReader;
import com.careir.utils.WaitUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Base for all page objects: shared driver, waits, and PageFactory initialization.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WaitUtil waitUtil;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.waitUtil = new WaitUtil(driver, ConfigReader.getInt("timeout"));
        PageFactory.initElements(driver, this);
    }
}
