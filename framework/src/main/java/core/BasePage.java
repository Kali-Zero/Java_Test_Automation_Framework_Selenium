package core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BasePage{
    public WebElement usernameField(WebDriver driver){return driver.findElement(By.id("LoginName"));}
    public WebElement passwordField(WebDriver driver){return driver.findElement(By.id("Password"));}
    public WebElement signInButton(WebDriver driver){return driver.findElement(By.id("btnSignIn"));}
}