package core;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.EdgeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import static java.lang.System.getProperty;
import static java.lang.System.setProperty;

public class BaseTest {

    private final BasePage basePage = new BasePage();
    public WebDriver driver;
    public ExtentTest test;
    public Properties prop = new Properties();
    public Properties browserProps = new Properties();
    private static ExtentReports extent;
    private final Dimension d = new Dimension(1280,960);
    private final String extentFolder = "./../ExtentReports/";
    private final String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh-mm a"));

    private String reportTitle()throws IOException {
        prop.load(new FileInputStream(new File("./../framework/src/main/java/resources/config.properties")));
        prop.load(new FileInputStream(new File("./../framework/src/main/java/resources/browser.properties")));
        String env = "Unknown";
        //String webBrowser = prop.getProperty("browser");
        if     (prop.getProperty("base_url").contains(prop.getProperty("env_dev")))  {env = "Development";}
        else if(prop.getProperty("base_url").contains(prop.getProperty("env_prod"))) {env = "Production";}
        else if(prop.getProperty("base_url").contains(prop.getProperty("env_local"))){env = "Local";}
        return timeStamp+" - Automation Report - "+env+ " - " +prop.getProperty("browser");
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
        //Checks to see if that stupid chrome cert page appears, and if it does, click through it so I can run my tests
        if(driver.findElement(By.xpath("//*[text()[contains(.,'Your connection is not private')]]")).isDisplayed()){
            driver.findElement(By.id("details-button")).click();
            driver.findElement(By.id("proceed-link")).click();
        }
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
    public void beforeMethod(Method method) throws IOException {

        //For Grid Functionality
        //threadDriver = new ThreadLocal<RemoteWebDriver>();
        //DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        browserProps.load(new FileInputStream(new File("./../framework/src/main/java/resources/browser.properties")));
        prop.load(new FileInputStream(new File("./../framework/src/main/java/resources/config.properties")));
        test = extent.startTest((this.getClass().getSimpleName() + " - " + method.getName()));
        switch (browserProps.getProperty("browser")) {
            case "Chrome":

                //TODO=============================Fix the download folder==============================================
                //Downloads the file... but puts it in the wrong place: \regressiontests\TestDownloadFolder
                //System.getProperty("user.dir") +File.separator +"TestDownloadFolder";

                //Puts the folder in the right place, but doesn't put the file in it.
                //System.getProperty("user.dir") +extentFolder +reportTitle() +"/TestDownloads";

                //Current test file download location:
                String testDownloadFolder = getProperty("user.dir")
                        +"."+File.separator+".."+File.separator
                        +"ExtentReports"+File.separator+reportTitle() +File.separator
                        +"TestDownloadFolder";
                //TODO==================================================================================================

                WebDriverManager.chromedriver().version(browserProps.getProperty("chromedriver_version")).setup();
                setProperty("is_headless", browserProps.getProperty("is_headless"));
                String chrome_headless = getProperty("is_headless");
                ChromeDriverManager.chromedriver();
                ChromeOptions chromeOptions = new ChromeOptions();
                Map<String, Object> prefs = new HashMap<>();
                chromeOptions.setExperimentalOption("prefs", prefs);
                prefs.put("profile.default_content_settings.popups", 0);
                prefs.put("download.default_directory",testDownloadFolder);
                if ("true".equals(chrome_headless)) {
                    chromeOptions.addArguments("--headless", "--no-sandbox");
                    driver = new ChromeDriver(chromeOptions);
                } else {driver = new ChromeDriver();}
                break;
            case "Firefox":
                WebDriverManager.firefoxdriver().version(browserProps.getProperty("firefox_version")).setup();
                setProperty("is_headless", browserProps.getProperty("is_headless"));
                String firefox_headless = getProperty("is_headless");
                FirefoxDriverManager.firefoxdriver();
                if ("true".equals(firefox_headless)) {
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("--headless");
                    driver = new FirefoxDriver(firefoxOptions);
                } else {driver = new FirefoxDriver();}
                break;
            case "MSEdge":
                WebDriverManager.edgedriver().version(browserProps.getProperty("ms_edge_version")).setup();
                setProperty("is_headless", browserProps.getProperty("is_headless"));
                String msEdge_headless = getProperty("is_headless");
                EdgeDriverManager.edgedriver();
                EdgeOptions edgeOptions = new EdgeOptions();

                //TODO: Why won't it run headless?!
                if ("true".equals(msEdge_headless)) {
                    //edgeOptions.addArguments("--headless", true);
                    edgeOptions.setCapability("--headless", true);
                    driver = new EdgeDriver(edgeOptions);
                } else {driver = new EdgeDriver();}
                break;
            default:
                throw new IllegalStateException("Unexpected Browser Type: " + browserProps.getProperty("browser"));
        }
        driver.manage().window().setSize(d);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.get(prop.getProperty("base_url"));

        //For Grid Functionality
        //threadDriver.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), desiredCapabilities));
        //threadDriver.set(new RemoteWebDriver(new URL(prop.getProperty("base_url")), desiredCapabilities));
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
            //if (!prop.getProperty("browser").equals("Firefox")){driver.quit();}
            //Bug if firefox is selected (tries to quit twice) - https://github.com/mozilla/geckodriver/issues/1235
        }
    }

    @AfterSuite
    public void afterSuite(){extent.close();}
}