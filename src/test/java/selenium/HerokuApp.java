package selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

public class HerokuApp {

    WebDriver driver;
    WebDriverWait wait;
    Actions actions;
    JavascriptExecutor js;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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

        List<WebElement> deleteButtons = driver.findElements(By.xpath("//div[@id='elements"));
        //finish this test
        /*for(WebElement : deleteButtons){
            deleteButtons.click();
        }*/

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

    @Test
    public void checkboxes() throws InterruptedException {
        driver.get("https://the-internet.herokuapp.com/checkboxes");

        WebElement checkbox1 = driver.findElement(By.xpath("//input[1]"));
/*
        WebElement checkbox2 = driver.findElement(By.xpath("//input[2]"));
*/


        boolean checkbox1InitialState = checkbox1.isSelected();
/*
        boolean checkbox2InitialState = checkbox2.isSelected();
*/

        if (checkbox1InitialState) {
            checkbox1.click();
            Assert.assertFalse(checkbox1.isSelected());
        }

        Thread.sleep(1000);
    }
        /*if (checkBox1.isSelected())
        {
            checkBox1.click();
            Assert.assertTrue(!checkBox1.isSelected());
        }
        else
        {
            checkBox1.click();
            Assert.assertTrue(checkBox1.isSelected());
        }

        if (checkBox2.isSelected())
        {
            checkBox2.click();
            Assert.assertTrue(!checkBox2.isSelected());
        }
        else
        {
            checkBox2.click();
            Assert.assertTrue(checkBox2.isSelected());
        }*/

        @Test
        public void floatingMenu(){
            driver.get("https://the-internet.herokuapp.com/floating_menu");
            //assert floating element is there when opening the page
            WebElement homeButton = driver.findElement(By.xpath("//*[@id='menu']//a[text()='Home']"));
            Assert.assertTrue(homeButton.isDisplayed());

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,2000)");
            //assert home button is stil there when scrolling down
            Assert.assertTrue(homeButton.isDisplayed());

            //scroll the page up
            js.executeScript("window.scrollBy(0,-1000)");
            //add explicit wait!!!!
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[id='menu']//a[text()='Home']")));
            Assert.assertTrue(homeButton.isDisplayed());
        }

        @Test
        public void dynamicControls(){
            driver.get("https://the-internet.herokuapp.com/dynamic_controls");

            WebElement checkbox = driver.findElement(By.id("checkbox"));
            Assert.assertTrue(checkbox.isDisplayed());

            WebElement removeButton = driver.findElement(By.xpath("//button[text()='Remove']"));
            removeButton.click();

            WebElement loadingAnimation = driver.findElement(By.xpath("//div[@id='loading']"));
            wait.until(ExpectedConditions.invisibilityOf(loadingAnimation));

            Assert.assertFalse(checkbox.isDisplayed());
            Assert.assertEquals(driver.findElement(By.id("message")).getText(), "It's gone!");
        }
}
