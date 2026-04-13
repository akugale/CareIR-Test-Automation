# CareIR Test Automation Framework

## Run Tests

- Run full suite:
  - `mvn clean test`
- Run in headless mode:
  - `mvn clean test -Dheadless=true`
- Run with a different URL:
  - `mvn clean test -DbaseUrl=https://your-app-url/login`

## Excel Test Data

- Framework expects: `src/test/resources/testdata/LoginData.xlsx`
- Sheet name: `Login`
- First row must be headers.
- If Excel is missing, framework uses one default demo row to keep execution functional.

## Execution Flow

1. TestNG reads `testng.xml` and starts tests in parallel.
2. `BaseTest` triggers `DriverFactory` to create a thread-safe WebDriver.
3. Test method gets credentials from Excel DataProvider.
4. `LoginPage` performs login and returns `DashboardPage`.
5. Assertion validates successful dashboard visibility.
6. Listener updates Extent report and captures screenshot on failure.
