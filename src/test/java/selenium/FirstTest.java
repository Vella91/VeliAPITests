package selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class FirstTest {

    ChromeDriver driver;

    public static void main(String[] args) throws InterruptedException {

        //1st approach - setting chrome driver manually
/*
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
*/
        //2nd approach - use WebDriverManager library
        WebDriverManager.chromedriver().setup();
        //initializing the driver
        ChromeDriver driver = new ChromeDriver();

        //opening a Chrome window
        driver.get("http://training.skillo-bg.com/posts/all");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        //creating a WebElement and then use the WebElement to click on it
        WebElement loginButton = driver.findElement(By.id("nav-link-login"));
        //list of elements
/*
        List<WebElement> loginButtons = driver.findElements(By.id("nav-ling-login"));
*/
        loginButton.click();

        WebElement usernameField = driver.findElement(By.id("defaultLoginFormUsername"));
        usernameField.sendKeys("test91");
        WebElement passwordField = driver.findElement(By.id("defaultLoginFormPassword"));
        passwordField.sendKeys("test91");
        WebElement signInButton = driver.findElement(By.id("sign-in-button"));
        signInButton.click();
        WebElement newPostButton = driver.findElement(By.id("nav-link-new-post"));
        Assert.assertTrue(newPostButton.isDisplayed());

        //bad practice, use proper waits
      /*  Thread.sleep(5000);*/
        //always close the browser after test
        driver.close();
    }
}
