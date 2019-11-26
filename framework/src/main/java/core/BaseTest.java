package core;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    private BasePage basePage = new BasePage();
    public WebDriver driver;
    public ExtentTest test;
    public Properties prop = new Properties();
    private static ExtentReports extent;
    private Dimension d = new Dimension(1280,960);
    private String extentFolder = "./../ExtentReports/";
    private String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh-mm a"));

    private String reportTitle()throws IOException {
        prop.load(new FileInputStream(new File("./../framework/src/main/java/resources/config.properties")));
        String env = "Unknown";
        if     (prop.getProperty("base_url").contains(prop.getProperty("env_dev")))  {env = "Development";}
        else if(prop.getProperty("base_url").contains(prop.getProperty("env_prod"))) {env = "Production";}
        else if(prop.getProperty("base_url").contains(prop.getProperty("env_local"))){env = "Local";}
        return timeStamp+" - Automation Report - "+env;
    }

    public void login(){
        basePage.usernameField(driver).sendKeys(prop.getProperty("base_username"));
        basePage.passwordField(driver).sendKeys(prop.getProperty("base_password"));
        basePage.signInButton(driver).click();
    }

    private String getScreenShot(String screenshotName) throws IOException{
        File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        //Relative path of the screenshot for the report (in Screenshots folder);
        String destination = reportTitle()+" - Screenshots/"+screenshotName+" - "+timeStamp+".png";
        //Where to initially save the screenshot:
        FileUtils.copyFile(source, new File(extentFolder+reportTitle()+"/"+reportTitle()+" - Screenshots/"+screenshotName+" - "+timeStamp+".png"));
        return destination;
    }

    @BeforeSuite
    public void beforeSuite()throws IOException{
        extent = new ExtentReports(extentFolder+reportTitle()+"/"+reportTitle()+".html");
        extent.loadConfig(new File("./../framework/src/main/java/resources/extent-config.xml"));
    }

    @BeforeMethod
    public void beforeMethod(Method method) throws IOException {
        prop.load(new FileInputStream(new File("./../framework/src/main/java/resources/config.properties")));
        test = extent.startTest((this.getClass().getSimpleName()+" - "+method.getName()));
        WebDriverManager.chromedriver().version(prop.getProperty("chromedriver_version")).setup();
        System.setProperty("is_headless", prop.getProperty("is_headless"));
        String headless = System.getProperty("is_headless");
        ChromeDriverManager.chromedriver();
        if("true".equals(headless)) {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--headless");
            driver = new ChromeDriver(chromeOptions);
        }else {driver = new ChromeDriver();}
        driver.manage().window().setSize(d);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.get(prop.getProperty("base_url"));
    }

    @AfterMethod
    public void afterMethod(ITestResult result) throws IOException{
        String description = result.getMethod().getDescription();
        if(result.getStatus() == ITestResult.SUCCESS) {
            test.log(LogStatus.PASS, description);
        }else if(result.getStatus() == ITestResult.FAILURE) {
            test.log(LogStatus.FAIL, description);
            test.log(LogStatus.FAIL, result.getThrowable().toString());
            test.log(LogStatus.FAIL, test.addScreenCapture(getScreenShot(result.getName())));
        }else if(result.getStatus() == ITestResult.SKIP) {
            test.log(LogStatus.SKIP, description);
        }else{
            test.log(LogStatus.UNKNOWN, description);
        }
        extent.endTest(test);
        extent.flush();
        if(driver != null) {
            driver.close();
            driver.quit();
        }
    }

    @AfterSuite
    public void afterSuite(){extent.close();}
}