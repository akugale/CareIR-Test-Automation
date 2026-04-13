package com.careir.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DashboardPage {

    @FindBy(xpath = "//*[contains(normalize-space(.),'Pending Tasks')]")
    private WebElement pendingTaskText;

    public DashboardPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public boolean isDashboardVisible() {
        try {
            return pendingTaskText != null && pendingTaskText.isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }
}
