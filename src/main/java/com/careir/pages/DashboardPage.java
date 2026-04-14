package com.careir.pages;

import com.careir.utils.ConfigReader;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Dashboard after successful login. Assertions use {@link #isDashboardVisible()}.
 */
public class DashboardPage extends BasePage {

    @FindBy(xpath = "//div[@class='msg-title']")
    private WebElement dashboardTitle;

    @FindBy(id = "userMenu")
    private WebElement userMenu;

    @FindBy(xpath = "//*[contains(normalize-space(.),'Messages/Pending Tasks')]")
    private WebElement messagesPendingTasksSection;

    @FindBy(xpath = "//*[contains(normalize-space(.),'Pending Tasks')]")
    private WebElement pendingTasksText;

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    /**
     * @return true if any known dashboard marker is visible within the configured timeout
     */
    public boolean isDashboardVisible() {
        long end = System.currentTimeMillis() + Duration.ofSeconds(ConfigReader.getInt("timeout")).toMillis();
        while (System.currentTimeMillis() < end) {
            if (isDisplayedQuick(dashboardTitle)
                    || isDisplayedQuick(userMenu)
                    || isDisplayedQuick(messagesPendingTasksSection)
                    || isDisplayedQuick(pendingTasksText)) {
                return true;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    public void openUserMenu() {
        waitUtil.waitForClickable(userMenu).click();
    }

    private static boolean isDisplayedQuick(WebElement element) {
        try {
            return element != null && element.isDisplayed();
        } catch (Exception ignored) {
            return false;
        }
    }
}
