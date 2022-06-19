package selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

public class HerokuApp {

    WebDriver driver;
    WebDriverWait wait;
    Actions actions;
    JavascriptExecutor js;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        //implicit wait is by default 0, implicit wait waits 20seconds before throwing No such element exception
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        //WebDriverWait explicit wait
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        actions = new Actions(driver);
    }

    @AfterMethod
    public void tearDown() {
        driver.close();
    }

    @Test(testName = "should have no delete buttons")
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

    @Test(testName = "add delete buttons and remove delete buttons after click on them")
    public void addRemoveElements2() throws InterruptedException {
        driver.get("https://the-internet.herokuapp.com/add_remove_elements/");
        //test logic

        WebElement addElementButton = driver.findElement(By.xpath("//button[@onclick='addElement()']"));

        for (int i = 0; i < 2; i++) {
            addElementButton.click();
        }
        List<WebElement> elementsContainerChildrenFilled = driver.findElements(By.xpath("//div[@id='elements']/descendant::*"));

        Assert.assertEquals(elementsContainerChildrenFilled.size(), 2);

        //finish this test
        List<WebElement> deleteButtons = driver.findElements(By.xpath("//div[@id='elements"));
        for(WebElement deleteButton : deleteButtons){
            deleteButton.click();
        }
        //assert at the end that the list of delete buttons is empty because we removed them
        Assert.assertTrue(deleteButtons.isEmpty());
    }

    @Test(testName = "find Congrats text after successful authentication")
    public void basicAuth() throws InterruptedException {
        driver.get("https://admin:admin@the-internet.herokuapp.com/basic_auth");
        WebElement text = driver.findElement(By.xpath("//div[@class='example']/p"));

        Assert.assertEquals(text.getText(), "Congratulations! You must have the proper credentials.");

        Thread.sleep(1000);
    }

    @Test(testName = "drag and drop elements")
    public void dragAndDrop() {
        driver.get("https://the-internet.herokuapp.com/drag_and_drop");

        WebElement elementA = driver.findElement(By.id("column-a"));
        WebElement elementB = driver.findElement(By.id("column-b"));

        actions.moveToElement(elementA).clickAndHold(elementA).moveToElement(elementB).release(elementB).build().perform();
        //another method from Iv's homework
        actions.dragAndDrop(elementA,elementB).perform();
        WebElement headerElementA = driver.findElement(By.xpath("//div[@id='column-a']/header"));

        //does not work as expected
        Assert.assertEquals(headerElementA.getText(),"B");
    }

    @Test(testName = "should context click on element and dismiss shown alert")
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

    @Test(testName = "should check status of checkboxes")
    public void checkboxes() throws InterruptedException {
        driver.get("https://the-internet.herokuapp.com/checkboxes");

        WebElement checkbox1 = driver.findElement(By.xpath("//input[1]"));

        if (checkbox1.isSelected()) {
            checkbox1.click();
            Assert.assertFalse(checkbox1.isSelected());
        }
        else
        {
            checkbox1.click();
            Assert.assertTrue(checkbox1.isSelected());
        }

        Thread.sleep(1000);
    }

    @Test(testName = "floating menu is working as expected with explicit wait for Home element")
    public void floatingMenu() {
        driver.get("https://the-internet.herokuapp.com/floating_menu");
        //assert floating element is there when opening the page
        WebElement homeButton = driver.findElement(By.xpath("//*[@id='menu']//a[text()='Home']"));
        Assert.assertTrue(homeButton.isDisplayed());

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,2000)");
        //assert home button is still there when scrolling down
        Assert.assertTrue(homeButton.isDisplayed());

        //scroll the page up
        js.executeScript("window.scrollBy(0,-1000)");
        //add explicit wait!!!!
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='menu']//a[text()='Home']")));
        Assert.assertTrue(homeButton.isDisplayed());
    }

    @Test(testName = "test with invisibility of element")
    public void dynamicControls() {
        driver.get("https://the-internet.herokuapp.com/dynamic_controls");

        WebElement checkbox = driver.findElement(By.id("checkbox"));
        Assert.assertTrue(checkbox.isDisplayed());

        WebElement removeButton = driver.findElement(By.xpath("//button[text()='Remove']"));
        removeButton.click();

        WebElement loadingAnimation = driver.findElement(By.xpath("//div[@id='loading']"));
        wait.until(ExpectedConditions.invisibilityOf(loadingAnimation));

        wait.until(ExpectedConditions.invisibilityOf(checkbox));
        Assert.assertEquals(driver.findElement(By.id("message")).getText(), "It's gone!");

        //fluent wait - custom logic for polling interval and wait time and ignoring exception class - for complex logic
      /*  Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);*/
        //wait until invisibility of element next
    }

    @Test(testName = "checks dynamic changes of elements - hello world is displayed")
    public void dynamicLoading() {
        driver.get("https://the-internet.herokuapp.com/dynamic_loading/2");

        By startButton = By.xpath("//div[@id='start']/button");
        By helloWorldText = By.xpath("//div[@id='finish']/h4");

        WebElement startButtonWebElement = driver.findElement(startButton);
        startButtonWebElement.click();
        WebElement helloWorldWebElement = driver.findElement(helloWorldText);
        Assert.assertTrue(helloWorldWebElement.isDisplayed());
    }

    @Test(testName = "iFrames switching")
    public void iFrames(){
        driver.get("https://the-internet.herokuapp.com/iframe");
        //switch to iframe to locate elements
        driver.switchTo().frame("mce-0-ifr");

        WebElement textElement = driver.findElement(By.xpath("//*[@id='tinymce']//p"));
        textElement.clear();
        textElement.sendKeys("test");

        //switch to default content to locate elements outside of the iframe
        driver.switchTo().defaultContent();
        WebElement headerText = driver.findElement(By.xpath("//div[@class='example']/h3"));
    }
}