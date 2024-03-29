package googlehomepagetests;

import com.relevantcodes.extentreports.LogStatus;
import googlepageobjects.GoogleHomePageObjects;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import core.BaseTest;

public class GoogleHomepageTests extends BaseTest {

    private final GoogleHomePageObjects googleHomePageObjects = new GoogleHomePageObjects();

    //Note: If outside the US, you may need to add a line to deal with a modal about accepting cookies.

    @Test (priority = 1, description ="Test 1: Does the 'About' link bring the user to the 'About' page?")
    public void doesAboutLinkWork() {
        WebDriverWait wait = new WebDriverWait(driver, 60);
        Assert.assertTrue(googleHomePageObjects.about(driver).isDisplayed());
        test.log(LogStatus.PASS, "'About' link is displayed.");
        wait.until(ExpectedConditions.elementToBeClickable(googleHomePageObjects.about(driver))).click();
        wait.until(ExpectedConditions.urlContains(prop.getProperty("about_url")));
        Assert.assertTrue(driver.getCurrentUrl().contains(prop.getProperty("about_url")), "'About' URL is incorrect!");
        test.log(LogStatus.PASS, "'About' URL is correct.");
    }

    @Test (priority = 2, description ="Test 2: Does the 'Store' link bring the user to the 'Store' page?")
    public void doesStoreLinkWork() {
        WebDriverWait wait = new WebDriverWait(driver, 60);
        Assert.assertTrue(googleHomePageObjects.store(driver).isDisplayed());
        test.log(LogStatus.PASS, "'Store' link is displayed.");
        wait.until(ExpectedConditions.elementToBeClickable(googleHomePageObjects.store(driver))).click();
        wait.until(ExpectedConditions.urlContains(prop.getProperty("store_url")));
        Assert.assertTrue(driver.getCurrentUrl().contains(prop.getProperty("store_url")), "'Store' URL is incorrect!");
        test.log(LogStatus.PASS, "'Store' URL is correct.");
    }

    @Test (priority = 3, description ="Test 3: Does the 'Gmail' link bring the user to the 'Gmail' page?")
    public void doesGmailLinkWork() {
        WebDriverWait wait = new WebDriverWait(driver, 60);
        Assert.assertTrue(googleHomePageObjects.gmail(driver).isDisplayed());
        test.log(LogStatus.PASS, "'Gmail' link is displayed.");
        wait.until(ExpectedConditions.elementToBeClickable(googleHomePageObjects.gmail(driver))).click();
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains(prop.getProperty("logged_in_gmail_url")),
                ExpectedConditions.urlContains(prop.getProperty("not_logged_in_gmail_url"))
        ));
        Assert.assertTrue(
                driver.getCurrentUrl().contains(prop.getProperty("logged_in_gmail_url")) ||
                driver.getCurrentUrl().contains(prop.getProperty("not_logged_in_gmail_url")),
                "'Gmail' URL is incorrect!");
        test.log(LogStatus.PASS, "'Gmail' URL is correct.");
    }

    @Test(priority = 4, description ="Test 4: Does the 'Images' link bring the user to the 'Images' page?")
    public void doesImagesLinkWork() {
        WebDriverWait wait = new WebDriverWait(driver, 60);
        Assert.assertTrue(googleHomePageObjects.images(driver).isDisplayed());
        test.log(LogStatus.PASS, "'Images' link is displayed.");
        wait.until(ExpectedConditions.elementToBeClickable(googleHomePageObjects.images(driver))).click();
        Assert.assertTrue(driver.getCurrentUrl().contains(prop.getProperty("images_url")), "'Images' URL is incorrect!");
        test.log(LogStatus.PASS, "'Images' URL is correct.");
    }

    //This is currently set to fail on purpose (to check Extent Report)
    @Test (priority = 5, description ="Test 5: Does the 'Apps' link open the 'Apps' modal? " +
            "Note: This test is set to fail so you can see error messaging and screenshots.")
    public void doesAppsLinkWork() {
        WebDriverWait wait = new WebDriverWait(driver, 60);
        Assert.assertTrue(googleHomePageObjects.apps(driver).isDisplayed());
        test.log(LogStatus.PASS, "'Apps' link is displayed.");
        wait.until(ExpectedConditions.elementToBeClickable(googleHomePageObjects.apps(driver))).click();
        driver.switchTo().activeElement();
        Assert.assertTrue(googleHomePageObjects.appsAccount(driver).isDisplayed());
        test.log(LogStatus.PASS, "This should not appear in report because it failed.");
    }

    @Test (priority = 6, description ="Test 6: Does the 'Sign In' button bring the user to the 'Sign In' page?")
    public void doesSignInButtonWork() {
        WebDriverWait wait = new WebDriverWait(driver, 60);
        Assert.assertTrue(googleHomePageObjects.signIn(driver).isDisplayed());
        test.log(LogStatus.PASS, "'Sign In Button' link is displayed.");
        wait.until(ExpectedConditions.elementToBeClickable(googleHomePageObjects.signIn(driver))).click();
        Assert.assertTrue(driver.getCurrentUrl().contains(prop.getProperty("account_url")), "'Account' URL is incorrect!");
        test.log(LogStatus.PASS, "Account text is displayed.");
    }

    @Test (priority = 7, description ="Test 7: This test is set to be skipped.")
    public void skipTeEst() {
        test.log(LogStatus.SKIP, "This test is set to be skipped.");
    }
}
