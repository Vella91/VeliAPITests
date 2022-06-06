package selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class FirstTest {

    public static void main(String[] args) throws InterruptedException {

        ChromeDriver driver = new ChromeDriver();


        //1st approach - setting chrome driver manually
/*
        System.setProperty("webdriver.chrome.drive", "drivers/chromedriver.exe");
*/

        //2nd approach - use WebDriverManager library
        WebDriverManager.chromedriver().setup();

        //opening a Chrome window
        driver.get("http://training.skillo-bg.com/posts/all");
        driver.manage().window().maximize();

        //creating a WebElement and then use the WebElement to click on it
        WebElement loginButton = driver.findElement(By.id("nav-link-login"));
        //list of elements
        List<WebElement> loginButtons = driver.findElements(By.id("nav-ling-login"));
        loginButton.click();


        //bad practice, use proper waits
        Thread.sleep(5000);
        //alsways close the browser after test
        driver.close();
    }
}
