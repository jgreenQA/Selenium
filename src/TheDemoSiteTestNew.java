import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.apache.commons.io.FileUtils;
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
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TheDemoSiteTestNew {
    private WebDriver wd;
    private Random r;

    private LoginPage loginPage;
    private CreateUserPage createUserPage;

    private Wait<WebDriver> wait;

    private static final ExtentReports extent = new ExtentReports();
    private static ExtentTest test;

    @BeforeClass
    public static void startReport() {
        ExtentHtmlReporter extentHtmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + File.separatorChar + "test-output" + File.separatorChar + "report.html");
        extentHtmlReporter.config().setReportName("Report");
        extentHtmlReporter.config().setDocumentTitle("Example Test");
        extent.attachReporter(extentHtmlReporter);
        test = extent.createTest("Test");
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
        wd.manage().window().maximize();
        String randUser = generateString(); test.log(Status.DEBUG, "Username Generated: " + randUser);
        String randPass = generateString(); test.log(Status.DEBUG, "Password Generated: " + randPass);

        wd.navigate().to("http://www.thedemosite.co.uk/addauser.php"); test.log(Status.INFO, "Navigated to User Creation Page");
        createUserPage.enterUsername(randUser); test.log(Status.INFO, "Entered Username");
        createUserPage.enterPassword(randPass); test.log(Status.INFO, "Entered Password");
        createUserPage.clickSubmitButton(); test.log(Status.INFO, "Submit Button Clicked");

        wd.navigate().to("http://www.thedemosite.co.uk/login.php"); test.log(Status.INFO, "Navigated to Login Page");
        loginPage.enterUsername(randUser); test.log(Status.INFO, "Entered Username");
        loginPage.enterPassword(randPass); test.log(Status.INFO, "Entered Password");
        loginPage.clickSubmitButton(); test.log(Status.INFO, "Submit Button Clicked");

        try {
            Assert.assertEquals("Login Unsuccessful", "**Successful Login**", wait.until(webDriver -> loginPage.getLoginStatusText()));
            test.log(Status.PASS,"Test Passed");
        } catch (AssertionError ex) {
            test.log(Status.ERROR, ex);
            test.log(Status.FAIL,"Test Failed");
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

    public static String takeScreenshot(WebDriver webDriver, String fileName) throws IOException {
        File scrFile = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
        String pathname = System.getProperty("user.dir") + File.separatorChar + fileName +".jpg";
        FileUtils.copyFile(scrFile, new File(pathname));
        return pathname;
    }
}