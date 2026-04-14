package com.careir.pages;

import com.careir.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import java.time.Duration;
import java.util.List;

public class LoginPage extends BasePage {

    @FindBys({
            @FindBy(id = "username")
    })
    private List<WebElement> usernameById;

    @FindBys({
            @FindBy(name = "username")
    })
    private List<WebElement> usernameByName;

    @FindBys({
            @FindBy(name = "email")
    })
    private List<WebElement> usernameByEmailName;

    @FindBys({
            @FindBy(css = "input[type='email']")
    })
    private List<WebElement> usernameByEmailType;

    @FindBys({
            @FindBy(id = "password")
    })
    private List<WebElement> passwordById;

    @FindBys({
            @FindBy(name = "password")
    })
    private List<WebElement> passwordByName;

    @FindBys({
            @FindBy(css = "input[type='password']")
    })
    private List<WebElement> passwordByType;

    @FindBys({
            @FindBy(css = "button[type='submit']")
    })
    private List<WebElement> submitButtons;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(ConfigReader.get("baseUrl"));
        bypassPrivacyInterstitialIfPresent();
        return this;
    }

    private void bypassPrivacyInterstitialIfPresent() {
        if (isLoginFormVisible()) {
            return;
        }

        String title = driver.getTitle() == null ? "" : driver.getTitle().toLowerCase();
        String pageSource = driver.getPageSource() == null ? "" : driver.getPageSource().toLowerCase();
        boolean privacyErrorPage = title.contains("privacy error")
                || pageSource.contains("your connection is not private")
                || driver.getCurrentUrl().startsWith("chrome-error://");

        if (privacyErrorPage) {
            new Actions(driver).sendKeys("thisisunsafe").perform();
            waitForAnyVisible(getUsernameCandidates());
        }
    }

    private boolean isLoginFormVisible() {
        try {
            return findFirstVisible(getUsernameCandidates()) != null;
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

    public LoginPage enterUsername(String username) {
        WebElement usernameInput = waitForAnyVisible(getUsernameCandidates());
        usernameInput.clear();
        usernameInput.sendKeys(username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        WebElement passwordInput = findPasswordInput();
        if (passwordInput == null) {
            passwordInput = waitForAnyVisible(getPasswordCandidates());
        }
        passwordInput.clear();
        passwordInput.sendKeys(password);
        return this;
    }

    public DashboardPage clickLogin() {
        WebElement button = waitForAnyVisible(getSubmitCandidates());
        waitUtil.waitForClickable(button).click();
        return new DashboardPage(driver);
    }

    public DashboardPage loginAs(String username, String password) {
        return enterUsername(username)
                .enterPassword(password)
                .clickLogin();
    }

    private WebElement findPasswordInput() {
        WebElement username = findFirstVisible(getUsernameCandidates());
        if (username == null) {
            return null;
        }

        WebElement form = findClosestForm(username);
        if (form == null) {
            return null;
        }

        List<WebElement> passwordFields = form.findElements(By.cssSelector("input[type='password']"));
        for (WebElement field : passwordFields) {
            if (field.isDisplayed()) {
                return field;
            }
        }
        return null;
    }

    private WebElement findClosestForm(WebElement field) {
        Object form = ((JavascriptExecutor) driver).executeScript(
                "return arguments[0] ? arguments[0].closest('form') : null;", field);
        return form instanceof WebElement ? (WebElement) form : null;
    }

    private WebElement waitForAnyVisible(List<WebElement> candidates) {
        long timeoutMillis = Duration.ofSeconds(ConfigReader.getInt("timeout")).toMillis();
        long endTime = System.currentTimeMillis() + timeoutMillis;

        while (System.currentTimeMillis() < endTime) {
            WebElement element = findFirstVisible(candidates);
            if (element != null) {
                return element;
            }

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Interrupted while waiting for login elements", e);
            }
        }
        throw new org.openqa.selenium.TimeoutException("Could not find visible login element");
    }

    private WebElement findFirstVisible(List<WebElement> elements) {
        for (WebElement element : elements) {
            if (element.isDisplayed()) {
                return element;
            }
        }
        return null;
    }

    private List<WebElement> getUsernameCandidates() {
        if (findFirstVisible(usernameById) != null) return usernameById;
        if (findFirstVisible(usernameByName) != null) return usernameByName;
        if (findFirstVisible(usernameByEmailName) != null) return usernameByEmailName;
        return usernameByEmailType;
    }

    private List<WebElement> getPasswordCandidates() {
        if (findFirstVisible(passwordById) != null) return passwordById;
        if (findFirstVisible(passwordByName) != null) return passwordByName;
        return passwordByType;
    }

    private List<WebElement> getSubmitCandidates() {
        if (findFirstVisible(submitButtons) != null) {
            return submitButtons;
        }
        return driver.findElements(By.cssSelector("input[type='submit'],button"));
    }
}
