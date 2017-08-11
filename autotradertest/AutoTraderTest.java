package autotradertest;

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
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AutoTraderTest {
    private WebDriver wd;
    private HomePage homePage;
    private CarsPage carsPage;
    private SearchPage searchPage;
    private Wait<WebDriver> wait;
    private static int counter = 0;

    private static final ExtentReports EXTENT = new ExtentReports();
    private static final String EXCEL_FILE_PATH = System.getProperty("user.dir") + File.separatorChar + "src" + File.separatorChar + "main" + File.separatorChar + "resources" + File.separatorChar + "test-autotrader.xlsx";

    private static ArrayList<String> testValues;
    private static ArrayList<String> testExpected;
    private static HashMap<String, ExtentTest> tests = new HashMap<>();
    private static ExtentTest test;

    @BeforeClass
    public static void init() {
        ExtentHtmlReporter extentHtmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + File.separatorChar + "test-output" + File.separatorChar + "autotrader_report.html");
        extentHtmlReporter.config().setReportName("AutoTrader Report");
        extentHtmlReporter.config().setDocumentTitle("AutoTrader Test");
        EXTENT.attachReporter(extentHtmlReporter);
    }

    @Before
    public void setup() {
        incrementCounter();
        testValues = getSpreadSheetValuesFromSheet(0, counter);
        testExpected = getSpreadSheetValuesFromSheet(1, counter);
        test = EXTENT.createTest(testValues.get(0));
        wd = new ChromeDriver();

        homePage = PageFactory.initElements(wd, HomePage.class);
        carsPage = PageFactory.initElements(wd, CarsPage.class);
        searchPage = PageFactory.initElements(wd, SearchPage.class);

        wait = new FluentWait<>(wd)
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(5, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);
    }

    @Test
    public void postcodeEntryTest() {
        wd.manage().window().maximize();

        String postcode = testValues.get(2);
        test.log(Status.DEBUG, "Post Code: " + postcode);

        wd.navigate().to("http://www.autotrader.co.uk/");
        test.log(Status.INFO, "Navigated to Home Page");
        homePage.enterPostcode(postcode);
        homePage.clickSearchButton();
    }

    @Test
    public void postcodePopUpEntryTest() {
        wd.manage().window().maximize();

        String postcode = testValues.get(2);
        test.log(Status.DEBUG, "Post Code: " + postcode);

        wd.navigate().to("http://www.autotrader.co.uk/");
        test.log(Status.INFO, "Navigated to Home Page");

        try {
            homePage.clickSearchButton();
            homePage.enterPostcodePopUp(postcode);
            homePage.clickPostcodePopUpButton();

            Assert.assertEquals("Test Unsuccessful", testExpected.get(1), wait.until(webDriver -> carsPage.getBreadcrumbText()));
            test.log(Status.PASS, "Test Passed");
        } catch (AssertionError | NoSuchElementException ex) {
            test.log(Status.ERROR, ex);
            test.log(Status.FAIL, "Test Failed");
        } finally {
            try {
                test.addScreenCaptureFromPath(takeScreenshot(wd, "screenshot"));
            } catch (IOException ex) {
                test.log(Status.WARNING, "Screenshot could not be taken");
            }
        }
    }

    @Test
    public void selectCar1Test() {
        wd.manage().window().maximize();

        String postcode = testValues.get(2);
        test.log(Status.DEBUG, "Post Code: " + postcode);

        wd.navigate().to("http://www.autotrader.co.uk/");
        test.log(Status.INFO, "Navigated to Home Page");
        homePage.enterPostcode(postcode);
        homePage.clickSearchButton();

        //carsPage.clickCar1();

        test.log(Status.FAIL, "Test Failed");
        Assert.fail();
    }

    @Test
    public void paginationTest() {
        wd.manage().window().maximize();

        String postcode = testValues.get(2);
        test.log(Status.DEBUG, "Post Code: " + postcode);

        wd.navigate().to("http://www.autotrader.co.uk/");
        test.log(Status.INFO, "Navigated to Home Page");
        homePage.enterPostcode(postcode);
        homePage.clickSearchButton();

        //carsPage.clickPageRight();

        test.log(Status.FAIL, "Test Failed");
        Assert.fail();
    }

    @Test
    public void filteringTest() {
        wd.manage().window().maximize();

        String postcode = testValues.get(2);
        test.log(Status.DEBUG, "Post Code: " + postcode);

        wd.navigate().to("http://www.autotrader.co.uk/");
        test.log(Status.INFO, "Navigated to Home Page");
        homePage.enterPostcode(postcode);
        homePage.clickSearchButton();

        //carsPage.clickFilter();

        test.log(Status.FAIL, "Test Failed");
        Assert.fail();
    }

    @Test
    public void carSearchTest1() {
        wd.manage().window().maximize();

        String postcode = testValues.get(2);
        String keywords = testValues.get(3);
        test.log(Status.DEBUG, "Post Code: " + postcode);
        test.log(Status.DEBUG, "Keywords: " + keywords);

        wd.navigate().to("http://www.autotrader.co.uk/");
        test.log(Status.INFO, "Navigated to Home Page");

        Actions a = new Actions(wd);
        a.moveToElement(wd.findElement(By.cssSelector("#js-header-nav > ul > li:nth-child(1) > a > span"))).moveByOffset(0, 50).click().build().perform();

        searchPage.enterPostcode(postcode);
        searchPage.enterKeywords(keywords);
        searchPage.clickSearchButton();

        try {
            Assert.assertEquals("Test Unsuccessful", testExpected.get(1), wait.until(webDriver -> carsPage.getBreadcrumbText()));
            test.log(Status.PASS, "Test Passed");
        } catch (AssertionError ex) {
            test.log(Status.ERROR, ex);
            test.log(Status.FAIL, "Test Failed");
        } finally {
            try {
                test.addScreenCaptureFromPath(takeScreenshot(wd, "screenshot"));
            } catch (IOException ex) {
                test.log(Status.WARNING, "Screenshot could not be taken");
            }
        }
    }

    @Test
    public void carSearchTest2() {
        wd.manage().window().maximize();

        String postcode = testValues.get(2);
        String keywords = testValues.get(3);
        test.log(Status.DEBUG, "Post Code: " + postcode);
        test.log(Status.DEBUG, "Keywords: " + keywords);

        wd.navigate().to("http://www.autotrader.co.uk/");
        test.log(Status.INFO, "Navigated to Home Page");

        Actions a = new Actions(wd);
        a.moveToElement(wd.findElement(By.cssSelector("#js-header-nav > ul > li:nth-child(1) > a > span"))).moveByOffset(0, 50).click().build().perform();

        searchPage.enterPostcode(postcode);
        searchPage.enterKeywords(keywords);
        searchPage.clickSearchButton();

        try {
            Assert.assertEquals("Test Unsuccessful", testExpected.get(1), wait.until(webDriver -> carsPage.getBreadcrumbText()));
            test.log(Status.PASS, "Test Passed");
        } catch (AssertionError ex) {
            test.log(Status.ERROR, ex);
            test.log(Status.FAIL, "Test Failed");
        } finally {
            try {
                test.addScreenCaptureFromPath(takeScreenshot(wd, "screenshot"));
            } catch (IOException ex) {
                test.log(Status.WARNING, "Screenshot could not be taken");
            }
        }
    }

    @Test
    public void carSearchTest3() {
        wd.manage().window().maximize();

        String postcode = testValues.get(2);
        String keywords = testValues.get(3);
        test.log(Status.DEBUG, "Post Code: " + postcode);
        test.log(Status.DEBUG, "Keywords: " + keywords);

        wd.navigate().to("http://www.autotrader.co.uk/");
        test.log(Status.INFO, "Navigated to Home Page");

        Actions a = new Actions(wd);
        a.moveToElement(wd.findElement(By.cssSelector("#js-header-nav > ul > li:nth-child(1) > a > span"))).moveByOffset(0, 50).click().build().perform();

        searchPage.enterPostcode(postcode);
        searchPage.enterKeywords(keywords);
        searchPage.clickSearchButton();

        try {
            Assert.assertEquals("Test Unsuccessful", testExpected.get(1), wait.until(webDriver -> carsPage.getBreadcrumbText()));
            test.log(Status.PASS, "Test Passed");
        } catch (AssertionError ex) {
            test.log(Status.ERROR, ex);
            test.log(Status.FAIL, "Test Failed");
        } finally {
            try {
                test.addScreenCaptureFromPath(takeScreenshot(wd, "screenshot"));
            } catch (IOException ex) {
                test.log(Status.WARNING, "Screenshot could not be taken");
            }
        }
    }

    @Test
    public void carSearchTest4() {
        wd.manage().window().maximize();

        String postcode = testValues.get(2);
        String keywords = testValues.get(3);
        test.log(Status.DEBUG, "Post Code: " + postcode);
        test.log(Status.DEBUG, "Keywords: " + keywords);

        wd.navigate().to("http://www.autotrader.co.uk/");
        test.log(Status.INFO, "Navigated to Home Page");

        Actions a = new Actions(wd);
        a.moveToElement(wd.findElement(By.cssSelector("#js-header-nav > ul > li:nth-child(1) > a > span"))).moveByOffset(0, 50).click().build().perform();

        searchPage.enterPostcode(postcode);
        searchPage.enterKeywords(keywords);
        searchPage.clickSearchButton();

        try {
            Assert.assertEquals("Test Unsuccessful", testExpected.get(1), wait.until(webDriver -> carsPage.getBreadcrumbText()));
            test.log(Status.PASS, "Test Passed");
        } catch (AssertionError ex) {
            test.log(Status.ERROR, ex);
            test.log(Status.FAIL, "Test Failed");
        } finally {
            try {
                test.addScreenCaptureFromPath(takeScreenshot(wd, "screenshot"));
            } catch (IOException ex) {
                test.log(Status.WARNING, "Screenshot could not be taken");
            }
        }
    }

    @Test
    public void carSearchTest5() {
        wd.manage().window().maximize();

        String postcode = testValues.get(2);
        String keywords = testValues.get(3);
        test.log(Status.DEBUG, "Post Code: " + postcode);
        test.log(Status.DEBUG, "Keywords: " + keywords);

        wd.navigate().to("http://www.autotrader.co.uk/");
        test.log(Status.INFO, "Navigated to Home Page");

        Actions a = new Actions(wd);
        a.moveToElement(wd.findElement(By.cssSelector("#js-header-nav > ul > li:nth-child(1) > a > span"))).moveByOffset(0, 50).click().build().perform();

        searchPage.enterPostcode(postcode);
        searchPage.enterKeywords(keywords);
        searchPage.clickSearchButton();

        try {
            Assert.assertEquals("Test Unsuccessful", testExpected.get(1), wait.until(webDriver -> carsPage.getBreadcrumbText()));
            test.log(Status.PASS, "Test Passed");
        } catch (AssertionError ex) {
            test.log(Status.ERROR, ex);
            test.log(Status.FAIL, "Test Failed");
        } finally {
            try {
                test.addScreenCaptureFromPath(takeScreenshot(wd, "screenshot"));
            } catch (IOException ex) {
                test.log(Status.WARNING, "Screenshot could not be taken");
            }
        }
    }

    @After
    public void tearDown() {
        wd.quit();
    }

    @AfterClass
    public static void endReport() {
        EXTENT.flush();
    }

    private static ArrayList<String> getSpreadSheetValuesFromSheet(int sheetNum, int rowNum) {
        ArrayList<String> testValues = new ArrayList<>();
        FileInputStream excelFile = null;

        try {
            excelFile = new FileInputStream(new File(EXCEL_FILE_PATH));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet sheet = workbook.getSheetAt(sheetNum);

            for (Row currentRow : sheet) {
                if (currentRow.getRowNum() == rowNum) {
                    for (Cell currentCell : currentRow) {
                        switch (currentCell.getCellTypeEnum()) {
                            case STRING:
                                testValues.add(currentCell.getStringCellValue());
                                break;
                            case NUMERIC:
                                testValues.add(String.valueOf(currentCell.getNumericCellValue()));
                                break;
                            case BOOLEAN:
                                testValues.add(String.valueOf(currentCell.getBooleanCellValue()));
                                break;
                            case BLANK:
                                testValues.add(currentCell.getStringCellValue());
                                break;
                            default:
                                break;
                        }
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

    private void incrementCounter() {
        counter++;
    }

    private static String takeScreenshot(WebDriver webDriver, String fileName) throws IOException {
        File scrFile = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
        String pathname = System.getProperty("user.dir") + File.separatorChar + fileName +".jpg";
        FileUtils.copyFile(scrFile, new File(pathname));
        return pathname;
    }
}