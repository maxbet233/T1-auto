import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.GeckoDriverInfo;

public class SelTest {

    WebDriver driver;
    @BeforeEach
    void setup(){
        driver = new FirefoxDriver();
        driver.get("https://the-internet.herokuapp.com/");
    }

    @Test
    void test(){
        WebElement inputButton = driver.findElement(By.xpath("//a[@href]='inputs'"));
        inputButton.click();
    }
}