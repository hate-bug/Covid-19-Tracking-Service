package UItest;

import com.Application.Tracking_System_Application;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.junit.jupiter.Testcontainers;
import javax.transaction.Transactional;
import static org.junit.Assert.assertTrue;

@Testcontainers
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = Tracking_System_Application.class)
@Transactional
public class Frontend_Test {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @Value("${spring.google.location}")
    private String googleLocation;

    @Value("${spring.ff.location}")
    private String ffLocation;

    @Test
    public void TestWithGoogle() throws Exception {
        System.setProperty("webdriver.chrome.driver", googleLocation);
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        options.addArguments("--disable-gpu");
        this.driver = new ChromeDriver(options);
        UserCreatePatientAndSubmitPatient();
    }

    @Test
    public void TestWithFireFox () throws Exception {
        System.setProperty("webdriver.gecko.driver", ffLocation);
        FirefoxOptions option = new FirefoxOptions();
        option.setHeadless(true);
        option.addArguments("--disable-gpu");
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
        driver.get("http://localhost:8080");
        assertTrue(driver.findElement(By.className("blog-header-logo")).getText().contains("Covid-19 Tracking System"));
        //Go to the create patient page
        driver.findElement(By.id("createpatient")).click();
        driver.findElement(By.id("addevent")).click();
        assertTrue(driver.findElement(By.id("eventtable")).getText().contains("Event name"));
        driver.findElement(By.name("eventname")).sendKeys("e1");
        driver.findElement(By.name("date")).sendKeys("2020-07-01");
        driver.findElement(By.name("address")).click();
        driver.findElement(By.name("address")).sendKeys("Carleton University, Ottawa, ON, Canada");

        //User click on the submit patient button and back to the homepage
        driver.findElement(By.id("submitpatient")).click();
        Thread.sleep(1000);
        driver.switchTo().alert().accept();
        assertTrue(driver.findElement(By.className("blog-header-logo")).getText().contains("Covid-19 Tracking System"));

        //go to events page and should be able to see the event just added
        //Longitude and latitude value should be automatically retrieved.
        Thread.sleep(4000);
        driver.findElement(By.id("showevents")).click();
        Thread.sleep(1000);
        assertTrue(driver.findElement(By.xpath("//table[@id='eventlist']")).getText().contains("Carleton University"));
        assertTrue(driver.findElement(By.xpath("//table[@id='eventlist']")).getText().contains("-75.69"));
        assertTrue(driver.findElement(By.xpath("//table[@id='eventlist']")).getText().contains("45.38"));
    }

    @After
    public void afterTest () {
        driver.quit();
    }
}