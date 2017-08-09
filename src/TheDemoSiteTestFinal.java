import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.junit.*;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TheDemoSiteTestFinal {
    private WebDriver wd;
    private Random r;

    private LoginPage loginPage;
    private CreateUserPage createUserPage;

    private Wait<WebDriver> wait;

    private static final ExtentReports extent = new ExtentReports();

    private static final String EXCEL_FILE_PATH = System.getProperty("user.dir") + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "test-values.xlsx";

    private static HashMap<String, List<String>> testsValues;
    private static HashMap<String, List<String>> testsExpected;
    private static HashMap<String, ExtentTest> tests = new HashMap<>();

    @BeforeClass
    public static void init() {
        ExtentHtmlReporter extentHtmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + File.separatorChar + "test-output" + File.separatorChar + "report.html");
        extentHtmlReporter.config().setReportName("Report");
        extentHtmlReporter.config().setDocumentTitle("Example Test");
        extent.attachReporter(extentHtmlReporter);

        testsValues = getSpreadSheetValuesFromSheet(0);
        testsExpected = getSpreadSheetValuesFromSheet(1);
        for (Map.Entry<String, List<String>> entry : testsValues.entrySet()) {
            tests.put(entry.getKey(), extent.createTest(entry.getKey()));
        }
    }

    @Before
    public void setup() {
        wd =  new ChromeDriver();
        r = new Random();

        createUserPage = PageFactory.initElements(wd, CreateUserPage.class);
        loginPage = PageFactory.initElements(wd, LoginPage.class);

        wait = new FluentWait<>(wd)
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(5, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);
    }

    @Test
    public void test() throws InterruptedException {
        for (Map.Entry<String, ExtentTest> entry : tests.entrySet()) {
            wd.manage().window().maximize();
            String user = testsValues.get(entry.getKey()).get(1); entry.getValue().log(Status.DEBUG, "Username Generated: " + user);
            String pass = testsValues.get(entry.getKey()).get(2); entry.getValue().log(Status.DEBUG, "Password Generated: " + pass);

            wd.navigate().to("http://www.thedemosite.co.uk/addauser.php"); entry.getValue().log(Status.INFO, "Navigated to User Creation Page");
            createUserPage.enterUsername(user); entry.getValue().log(Status.INFO, "Entered Username");
            createUserPage.enterPassword(pass); entry.getValue().log(Status.INFO, "Entered Password");
            createUserPage.clickSubmitButton(); entry.getValue().log(Status.INFO, "Submit Button Clicked");

            wd.navigate().to("http://www.thedemosite.co.uk/login.php"); entry.getValue().log(Status.INFO, "Navigated to Login Page");
            loginPage.enterUsername(user); entry.getValue().log(Status.INFO, "Entered Username");
            loginPage.enterPassword(pass); entry.getValue().log(Status.INFO, "Entered Password");
            loginPage.clickSubmitButton(); entry.getValue().log(Status.INFO, "Submit Button Clicked");

            try {
                Assert.assertEquals("Login Unsuccessful", testsExpected.get(entry.getKey()).get(0), wait.until(webDriver -> loginPage.getLoginStatusText()));
                entry.getValue().log(Status.PASS, "Test Passed");
            } catch (AssertionError ex) {
                entry.getValue().log(Status.ERROR, ex);
                entry.getValue().log(Status.FAIL, "Test Failed");
            } finally {
                try {
                    entry.getValue().addScreenCaptureFromPath(takeScreenshot(wd, "screenshot"));
                } catch (IOException ex) {
                    entry.getValue().log(Status.WARNING, "Screenshot could not be taken");
                }
            }
        }
    }

    @After
    public void tearDown() {
        wd.quit();
    }

    @AfterClass
    public static void endReport() {
        extent.flush();
    }

    private String generateString() {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        char[] text = new char[5];

        for (int i = 0; i < 5; i++) {
            text[i] = characters.charAt(r.nextInt(characters.length()));
        }
        return new String(text);
    }

    private static String takeScreenshot(WebDriver webDriver, String fileName) throws IOException {
        File scrFile = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
        String pathname = System.getProperty("user.dir") + File.separatorChar + fileName +".jpg";
        FileUtils.copyFile(scrFile, new File(pathname));
        return pathname;
    }

    private static HashMap<String, List<String>> getSpreadSheetValuesFromSheet(int sheetNum) {
        HashMap<String, List<String>> testValues = new HashMap<>();
        List<String> testValues_temp;
        FileInputStream excelFile = null;

        try {
            excelFile = new FileInputStream(new File(EXCEL_FILE_PATH));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(sheetNum);

            for (Row currentRow : sheet) {
                testValues_temp = new ArrayList<>();
                for (Cell currentCell : currentRow) {
                    switch (currentCell.getCellTypeEnum()) {
                        case STRING:
                            testValues_temp.add(currentCell.getStringCellValue());
                            break;
                        case NUMERIC:
                            testValues_temp.add(String.valueOf(currentCell.getNumericCellValue()));
                            break;
                        case BOOLEAN:
                            testValues_temp.add(String.valueOf(currentCell.getBooleanCellValue()));
                            break;
                        case BLANK:
                            testValues_temp.add(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    if (currentRow.getRowNum() != 0) {
                        testValues.put(testValues_temp.get(0), testValues_temp.subList(1, testValues_temp.size()));
                    }
                }
            }
            return testValues;
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println(EXCEL_FILE_PATH + " Could not be Opened");
            return null;
        } finally {
            try {
                excelFile.close();
            } catch (IOException | NullPointerException ex) {
                System.err.println(EXCEL_FILE_PATH + " Could not be Closed");
            }
        }
    }
}