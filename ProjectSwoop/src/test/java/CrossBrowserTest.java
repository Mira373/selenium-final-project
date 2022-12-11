import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.sun.istack.internal.NotNull;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.time.Duration;

public class CrossBrowserTest {
    WebDriver driver;
    @BeforeTest
    @Parameters("browser")
    public void setup(@NotNull String browser) throws Exception{

        //Check if parameter passed as 'chrome'
        if(browser.equalsIgnoreCase("chrome")){
            WebDriverManager.chromiumdriver().setup();
            driver = new ChromeDriver();
            //driver = new HtmlUnitDriver(BrowserVersion.CHROME);
        }
        //Check if parameter passed as 'ie'
        else if(browser.equalsIgnoreCase("ie")){
            WebDriverManager.iedriver().setup();
            driver = new InternetExplorerDriver();
            //driver = new HtmlUnitDriver(BrowserVersion.INTERNET_EXPLORER);

        }
        else{
            //If no browser passed throw exception
            throw new Exception("Browser is not correct");
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5000));
    }


    @Test
    public void verifyPageTitle() {
        SwoopProject monthFunction = new SwoopProject();
        monthFunction.openBrLink();
    }


    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}


