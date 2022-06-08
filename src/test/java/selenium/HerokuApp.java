package selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class HerokuApp {

    WebDriver driver;
    WebDriver wait;
    Actions actions;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        actions = new Actions(driver);
    }

    @AfterMethod
    public void tearDown() {
        driver.close();
    }

    @Test
    public void addRemoveElements1() throws InterruptedException {
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
    public void addRemoveElements2() throws InterruptedException {
        driver.get("https://the-internet.herokuapp.com/add_remove_elements/");
        //test logic
        List<WebElement> elementsContainerChildren = driver.findElements(By.xpath("//div[@id='elements']/descendant::*"));
        WebElement addElementbutton = driver.findElement(By.xpath("//button[@onclick='addElement()']"));

        for(int i=0;i<2;i++){
            addElementbutton.click();
        }
        elementsContainerChildren = driver.findElements(By.xpath("//div[@id='elements']/descendant::*"));

        Assert.assertEquals(elementsContainerChildren.size(), 2 );
        Thread.sleep(1000);
    }

    @Test
    public void basicAuth() throws InterruptedException {
     driver.get("https://admin:admin@the-internet.herokuapp.com/basic_auth");
     WebElement text = driver.findElement(By.xpath("//div[@class='example']/p"));

     Assert.assertEquals(text.getText(), "Congratulations! You must have the proper credentials.");

     Thread.sleep(1000);
    }

    @Test
    public void dragAndDrop(){
        driver.get("https://the-internet.herokuapp.com/drag_and_drop");

        WebElement elementA = driver.findElement(By.id("column-a"));
        WebElement elementB = driver.findElement(By.id("column-b"));

        actions.moveToElement(elementA).clickAndHold(elementA).moveToElement(elementB).release(elementB).build().perform();

    }

    @Test
    public void contextMenu() throws InterruptedException {
        driver.get("https://the-internet.herokuapp.com/context_menu");

        WebElement contextBox = driver.findElement(By.id("hot-spot"));
        actions.contextClick(contextBox).perform();
        Alert alert = driver.switchTo().alert();

        String alertText = alert.getText();
        Assert.assertEquals(alertText, "You selected a context menu");
        alert.dismiss();

        Thread.sleep(1000);
    }
}
