package com.careir.tests;

import com.careir.base.BaseTest;
import com.careir.listeners.RetryAnalyzer;
import com.careir.pages.DashboardPage;
import com.careir.pages.LoginPage;
import com.careir.utils.ConfigReader;
import com.careir.utils.ExcelUtil;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({com.careir.listeners.TestListener.class})
public class LoginTest extends BaseTest {

    @DataProvider(name = "loginData", parallel = true)
    public Object[][] loginData() {
        return ExcelUtil.getSheetData("testdata/LoginData.xlsx", "Login");
    }


    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void verifyVendorUserCanLoginFromProperties() {
        String username = ConfigReader.get("username");
        String password = ConfigReader.get("password");
        LoginPage loginPage = new LoginPage(getDriver()).open();
        DashboardPage dashboardPage = loginPage.loginAs(username, password);
        Assert.assertTrue(dashboardPage.isDashboardVisible(),
                "Dashboard should be visible for vendor user login");
    }
}
