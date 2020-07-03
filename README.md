**Kali's Test Automation Frame-Work-In-Progress**
---
    This is an example / template Test Automation Suite I built using:
        Language: Java (JDK 12)
        Framework: Selenium
        Dependancy Management: Maven
        Reporting: Extent Reports
    There is an example Report in the ExtentReports Folder if you wish to see 
    the results of running the suite.  If you have any questions or suggestions
    for improvements, let me know!
    Cheers!
        -Calista.Sullivan@gmail.com
**To Run Tests:**
---
    Open the TestNG.xml file in the regressiontests module
    Select the test classes you wish to run
    Right click anywhere in the file
    Select "Run"
    When complete, report will be in the ExtentReports folder
**Reference Links / Notes**
---
*JDK 12:*   
   [Oracle Java SE Downloads](https://www.oracle.com/technetwork/java/javase/downloads/index.html)

    (Download and install, then go to File->Project Structure->SDKs and 
    add the install directory there for IntelliJ.)
    
     Don't forget to add the Java and Maven entries to your 
     environment variables path (system variables - the one on the bottom)
     if you haven't already done so.
     Example Java & Maven System Variables (update as needed):
     
     Variable       Value
     JAVA_HOME      C:\Program Files\Java\jdk-12.0.1
     M2_HOME        C:\Program Files\Java\apache-maven-3.6.1\bin
     
     And add them to the "Path" variable: %JAVA_HOME% & %M2_HOME%

---
*Apache Maven (Make sure you download the .bin and not the .src zip file):*   
  [Apache Maven 3.6.1 Release Notes](https://maven.apache.org/docs/3.6.1/release-notes.html)  
  [Apache Maven Installation Guide - Windows](https://docs.wso2.com/display/IS323/Installing+Apache+Maven+on+Windows)
  
---
*Selenium:*   
   [Selenium Documentation](https://selenium.dev/)

---
*Extent Reports: (Current Version is 4 but I'm using 2)*    
   [Version 2 Documentation](http://extentreports.com/docs/versions/2/java/)    
   [Version 2 JAR files](https://mvnrepository.com/artifact/com.relevantcodes/extentreports/2.41.2)     
   
   [Version 4 Documentation](http://extentreports.com/docs/versions/4/java/)  
   [Version 4 JAR files](https://mvnrepository.com/artifact/com.aventstack/extentreports/4.0.9)
   
---
*Chromedriver Updates (Check periodically):*  
   [Boni García's WebDriverManager](https://github.com/bonigarcia/webdrivermanager/commit/0dfc857162a8aadb59d23b6c0cdd7a642b2b79b2)   
   Maintained by [Boni García](https://www.linkedin.com/in/bonigg/)
   
---  
   Here is the framework tutorial I based mine on - I found it VERY helpful:    
    [How to Develop a Test Automation Framework From Scratch?](https://www.testingexcellence.com/develop-test-automation-framework-scratch/)    
    Tutorial by [Amir Ghahrai](https://www.linkedin.com/in/aghahrai/)
    
---    
   Xpath Cheatsheets:   
    [5 Best XPath Cheat Sheets and Quick References](http://scraping.pro/5-best-xpath-cheat-sheets-and-quick-references/)   
    [DevHints Xpath CheatSheet](https://devhints.io/xpath)
    
---   
   Integrating Extent Reports:  
   [How to Generate Extent Reports in Selenium Webdriver](https://www.softwaretestingmaterial.com/generate-extent-reports/)   
   Tutorial by [Manikanta Rajkumar Seka](https://www.linkedin.com/in/rajkumarsm/   )
   
---           
   GitHub Readme Formatting:    
   [Writing on GitHub](https://help.github.com/en/github/writing-on-github)
   
---   
*My Favorite IDE:*  
    [Jetbrains' IntelliJ IDE](https://www.jetbrains.com/idea/features/)
