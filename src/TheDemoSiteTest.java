import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Random;

public class TheDemoSiteTest {
    private WebDriver wd;
    private Random r;

    @Before
    public void setup() {
        wd =  new ChromeDriver();
        r = new Random();
    }

    @Test
    public void test() throws InterruptedException {
        wd.manage().window().maximize();
        String randUser = generateString();

        wd.navigate().to("http://www.thedemosite.co.uk/addauser.php");
        wd.findElement(By.xpath("/html/body/table/tbody/tr/td[1]/form/div/center/table/tbody/tr/td[1]/div/center/table/tbody/tr[1]/td[2]/p/input")).sendKeys(randUser);
        wd.findElement(By.xpath("/html/body/table/tbody/tr/td[1]/form/div/center/table/tbody/tr/td[1]/div/center/table/tbody/tr[2]/td[2]/p/input")).sendKeys(randUser);
        wd.findElement(By.xpath("/html/body/table/tbody/tr/td[1]/form/div/center/table/tbody/tr/td[1]/div/center/table/tbody/tr[3]/td[2]/p/input")).click();

        wd.navigate().to("http://www.thedemosite.co.uk/login.php");
        wd.findElement(By.xpath("/html/body/table/tbody/tr/td[1]/form/div/center/table/tbody/tr/td[1]/table/tbody/tr[1]/td[2]/p/input")).sendKeys(randUser);
        wd.findElement(By.xpath("/html/body/table/tbody/tr/td[1]/form/div/center/table/tbody/tr/td[1]/table/tbody/tr[2]/td[2]/p/input")).sendKeys(randUser);
        wd.findElement(By.xpath("/html/body/table/tbody/tr/td[1]/form/div/center/table/tbody/tr/td[1]/table/tbody/tr[3]/td[2]/p/input")).click();

        Assert.assertEquals("Login Successful", "**Successful Login**", wd.findElement(By.cssSelector("body > table > tbody > tr > td.auto-style1 > big > blockquote > blockquote > font > center > b")).getText());
    }

    @After
    public void tearDown() {
        wd.quit();
    }

    private String generateString() {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        char[] text = new char[5];

        for (int i = 0; i < 5; i++) {
            text[i] = characters.charAt(r.nextInt(characters.length()));
        }
        return new String(text);
    }
}