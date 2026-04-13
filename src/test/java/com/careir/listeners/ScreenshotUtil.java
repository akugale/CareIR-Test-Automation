package com.careir.listeners;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class ScreenshotUtil {
    private ScreenshotUtil() {
    }

    public static String capture(WebDriver driver, String testName) {
        try {
            Path screenshotDir = Path.of("test-output", "screenshots");
            Files.createDirectories(screenshotDir);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String screenshotPath = screenshotDir.resolve(testName + "_" + timestamp + ".png").toString();

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(srcFile.toPath(), Path.of(screenshotPath));
            return screenshotPath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to capture screenshot.", e);
        }
    }
}
