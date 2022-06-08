package selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.List;

public class HerokuApp {

    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterMethod
    public void tearDown() {
        driver.close();
    }

    @Test
    public void addRemoveElements() throws InterruptedException {
        driver.get("https://the-internet.herokuapp.com/add_remove_elements/");
    /*    WebElement deleteButton = driver.findElement(By.cssSelector(".deleteButton"));
        Assert.assertFalse(deleteButton.isDisplayed());*/
/*
        Assert.assertFalse(driver.findElement(By.cssSelector(".added-manually")).isDisplayed());
*/
        //list of elements with xpath - wildcard
        List<WebElement> elementsContainerChildren = driver.findElements(By.xpath("//div[@id='elements']/descendant::*"));
        Assert.assertTrue(elementsContainerChildren.isEmpty());

        Thread.sleep(1000);
    }

    @Test
    public void addRemoveElements2() {
        driver.get("https://the-internet.herokuapp.com/add_remove_elements/");
        //test logic
        WebElement addElementbutton = driver.findElement(By.xpath("//button[@onclick=\"addElement()\"]"));
    }

}
