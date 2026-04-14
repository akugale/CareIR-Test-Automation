package com.careir.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ExtentReportManager {
    private static ExtentReports extentReports;

    private ExtentReportManager() {
    }

    public static ExtentReports getInstance() {
        if (extentReports == null) {
            try {
                Files.createDirectories(Path.of("test-output"));
            } catch (Exception e) {
                throw new RuntimeException("Unable to create report directory", e);
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String reportPath = "test-output/ExtentReport_" + timestamp + ".html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setReportName("CareIR E2E Automation Report");
            sparkReporter.config().setDocumentTitle("CareIR Test Execution");

            extentReports = new ExtentReports();
            extentReports.attachReporter(sparkReporter);
            extentReports.setSystemInfo("Framework", "Selenium + TestNG");
        }
        return extentReports;
    }
}
