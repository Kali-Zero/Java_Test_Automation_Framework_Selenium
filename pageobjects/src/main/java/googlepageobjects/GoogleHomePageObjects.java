package googlepageobjects;

import core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class GoogleHomePageObjects extends BasePage {

    //Top 'Nav Bar' Items
    public WebElement about(WebDriver driver){return driver.findElement(By.xpath(".//*[@id='hptl']//*[text()='About']"));}
    public WebElement store(WebDriver driver){return driver.findElement(By.xpath(".//*[@id='hptl']//*[text()='Store']"));}
    public WebElement gmail(WebDriver driver){return driver.findElement(By.xpath(".//*[@id='gbw']//*[text()='Gmail']"));}
    public WebElement images(WebDriver driver){return driver.findElement(By.xpath(".//*[@id='gbw']//*[text()='Images']"));}
    public WebElement apps(WebDriver driver){return driver.findElement(By.xpath(".//*[@title='Google apps']"));}
    public WebElement signIn(WebDriver driver){return driver.findElement(By.xpath(".//*[text()='Sign in']"));}

    //Apps menu elements
    public WebElement appsAccount(WebDriver driver){return driver.findElement(By.xpath(".//*[@aria-label='Google apps']//*[text()='Account']"));}

    //Bottom 'Footer' Items
}