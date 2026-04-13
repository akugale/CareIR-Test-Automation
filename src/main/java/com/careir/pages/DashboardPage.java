package com.careir.pages;

import com.careir.utils.ConfigReader;
import com.careir.utils.WaitUtil;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

public class DashboardPage {
    private final WaitUtil waitUtil;

    @FindBy(css = "h1.dashboard-title")
    private WebElement dashboardTitle;

    @FindBy(id = "userMenu")
    private WebElement userMenu;

    @FindBys({
            @FindBy(xpath = "//*[contains(translate(normalize-space(.),'DASHBOARD','dashboard'),'dashboard')]")
    })
    private List<WebElement> dashboardTextMarkers;

    @FindBys({
            @FindBy(xpath = "//div[contains(normalize-space(.),'Messages/Pending Tasks')]")
    })
    private List<WebElement> pendingTaskMarkers;

    public DashboardPage(WebDriver driver) {
        this.waitUtil = new WaitUtil(driver, ConfigReader.getInt("timeout"));
        PageFactory.initElements(driver, this);
    }

    public boolean isDashboardVisible() {
        if (isVisible(dashboardTitle) || isVisible(userMenu)) {
            return true;
        }
        return hasVisibleMarker(dashboardTextMarkers) || hasVisibleMarker(pendingTaskMarkers);
    }

    public void openUserMenu() {
        waitUtil.waitForClickable(userMenu).click();
    }

    private boolean hasVisibleMarker(List<WebElement> elements) {
        for (WebElement element : elements) {
            if (isVisible(element)) {
                return true;
            }
        }
        return false;
    }

    private boolean isVisible(WebElement element) {
        try {
            return element != null && element.isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }
}
