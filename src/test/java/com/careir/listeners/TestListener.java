package com.careir.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.careir.base.DriverFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static final ExtentReports EXTENT = ExtentReportManager.getInstance();
    private static final ThreadLocal<ExtentTest> TEST_NODE = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        TEST_NODE.set(EXTENT.createTest(result.getMethod().getMethodName()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        TEST_NODE.get().pass("Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String screenshotPath = ScreenshotUtil.capture(DriverFactory.getDriver(), result.getMethod().getMethodName());
        TEST_NODE.get().fail(result.getThrowable(),
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        TEST_NODE.get().skip("Test skipped: " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        EXTENT.flush();
    }
}
