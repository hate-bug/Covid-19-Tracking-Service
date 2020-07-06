package UItest;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.Assert.assertTrue;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Frontend_Test {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    /*@Test
    public void TestWithGoogle() throws Exception {
        System.setProperty("webdriver.chrome.driver", "/usr/local/share/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        this.driver = new ChromeDriver(options);
        UserCreatePatientAndSubmitPatient();
    }*/

    @Test
    public void TestWithFireFox () throws Exception {
        System.setProperty("webdriver.gecko.driver", "/usr/local/share/geckodriver");
        ProfilesIni profile = new ProfilesIni();
        FirefoxProfile myProfile = profile.getProfile("default");
        FirefoxOptions option = new FirefoxOptions();
        option.setProfile(myProfile);
        option.setHeadless(true);
        this.driver = new FirefoxDriver(option);
        UserCreatePatientAndSubmitPatient();
    }

    /**
     * User go to the homepage, click on the "Create Patient" button and create an association
     * then submit the patient. After that, user should be able to see created event list in event list.
     * @throws InterruptedException
     */

    public void UserCreatePatientAndSubmitPatient() throws InterruptedException {
        //Go to the homepage
        driver.get("http://127.0.0.1:8000");
        assertTrue(driver.findElement(By.className("blog-header-logo")).getText().contains("Covid-19-Tracking-System"));
        //Go to the create patient page
        driver.findElement(By.id("createpatient")).click();
        driver.findElement(By.id("addevent")).click();
        assertTrue(driver.findElement(By.id("eventtable")).getText().contains("Event name"));
        driver.findElement(By.name("name")).sendKeys("e1");
        driver.findElement(By.name("date")).sendKeys("20200701");
        driver.findElement(By.name("address")).click();
        driver.findElement(By.name("address")).sendKeys("Carleton University, Ottawa, ON, Canada");
        driver.findElement(By.name("address")).sendKeys(Keys.ENTER);

        //Longitude and latitude value should be automatically retrieved.
        Thread.sleep(2000);
        assertTrue(driver.findElement(By.className("latitude")).getAttribute("value").contains("45"));
        assertTrue(driver.findElement(By.className("longitude")).getAttribute("value").contains("75"));
        //User click on the submit patient button and back to the homepage
        driver.findElement(By.id("submitpatient")).click();
        Thread.sleep(1000);
        driver.switchTo().alert().accept();
        assertTrue(driver.findElement(By.className("blog-header-logo")).getText().contains("Covid-19-Tracking-System"));

        //go to events page and should be able to see the event just added
        driver.findElement(By.id("showevents")).click();
        Thread.sleep(1000);
        assertTrue(driver.findElement(By.xpath("//table[@id='eventlist']")).getText().contains("Carleton University"));
    }
}