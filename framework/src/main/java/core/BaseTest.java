package core;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    private final BasePage basePage = new BasePage();
    public WebDriver driver;
    public ExtentTest test;
    public Properties prop = new Properties();
    private static ExtentReports extent;
    private final Dimension d = new Dimension(1280,960);
    private final String extentFolder = "./../ExtentReports/";
    private final String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh-mm a"));

    private String reportTitle()throws IOException {
        prop.load(new FileInputStream(new File("./../framework/src/main/java/resources/config.properties")));
        String env = "Unknown";
        String webBrowser = prop.getProperty("browser");
        if     (prop.getProperty("base_url").contains(prop.getProperty("env_dev")))  {env = "Development";}
        else if(prop.getProperty("base_url").contains(prop.getProperty("env_prod"))) {env = "Production";}
        else if(prop.getProperty("base_url").contains(prop.getProperty("env_local"))){env = "Local";}
        return timeStamp+" - Automation Report - "+env+ " - " +webBrowser;
    }
    private String getScreenShot(String screenshotName) throws IOException{
        File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        //Relative path of the screenshot for the report (in Screenshots folder);
        String destination = reportTitle()+" - Screenshots/"+screenshotName+" - "+timeStamp+".png";
        //Where to initially save the screenshot:
        FileUtils.copyFile(source, new File(extentFolder+reportTitle()+"/"+reportTitle()+" - Screenshots/"+screenshotName+" - "+timeStamp+".png"));
        return destination;
    }
    public void login(){
        basePage.usernameField(driver).sendKeys(prop.getProperty("base_username"));
        basePage.passwordField(driver).sendKeys(prop.getProperty("base_password"));
        basePage.signInButton(driver).click();
    }

    @BeforeSuite
    public void beforeSuite()throws IOException{
        extent = new ExtentReports(extentFolder+reportTitle()+"/"+reportTitle()+".html");
        extent.loadConfig(new File("./../framework/src/main/java/resources/extent-config.xml"));
    }

    @BeforeMethod
    public FirefoxDriver beforeMethod(Method method) throws IOException {
        prop.load(new FileInputStream(new File("./../framework/src/main/java/resources/config.properties")));

        //===This will display the method name as the test title
        test = extent.startTest((this.getClass().getSimpleName()+" - "+method.getName()));

        //===This will display the method description as the test title (Not well formatted)
        //test = extent.startTest((this.getClass().getSimpleName()+" - "+method.getAnnotation(Test.class).description()));

        switch (prop.getProperty("browser")) {
            case "Chrome":
                WebDriverManager.chromedriver().version(prop.getProperty("chrome_version")).setup();
                System.setProperty("is_headless", prop.getProperty("is_headless"));
                String chrome_headless = System.getProperty("is_headless");
                ChromeDriverManager.chromedriver();
                if ("true".equals(chrome_headless)) {
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--headless");
                    driver = new ChromeDriver(chromeOptions);
                } else {driver = new ChromeDriver();}
                break;
            case "Firefox":
                WebDriverManager.firefoxdriver().version(prop.getProperty("firefox_version")).setup();
                System.setProperty("is_headless", prop.getProperty("is_headless"));
                String firefox_headless = System.getProperty("is_headless");
                FirefoxDriverManager.firefoxdriver();
                if ("true".equals(firefox_headless)) {
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("--headless");
                    driver = new FirefoxDriver(firefoxOptions);
                } else {driver = new FirefoxDriver();}
                break;
            case "MSEdge":  //TODO: Currently broken - fix later!!!
                WebDriverManager.edgedriver().version(prop.getProperty("edge_version")).setup();
                driver = new EdgeDriver();
                break;
            case "IE11":
                WebDriverManager.iedriver().version(prop.getProperty("ie_version")).setup();
                driver = new InternetExplorerDriver();
                break;
            default:
                throw new IllegalStateException("Unexpected Browser Type: " + prop.getProperty("browser"));
        }
        driver.manage().window().setSize(d);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.get(prop.getProperty("base_url"));
        return null;
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
            if (!prop.getProperty("browser").equals("Firefox")){driver.quit();}
            //Bug id firefox is selected (tries to quit twice) - https://github.com/mozilla/geckodriver/issues/1235
        }
    }

    @AfterSuite
    public void afterSuite(){extent.close();}
}