import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SelTest {

    WebDriver driver;
    @BeforeEach
    void setup(){
        driver = new ChromeDriver();
        driver.get("https://the-internet.herokuapp.com/");
    }

    @Test
    void test(){
        WebElement inputButton = driver.findElement(By.xpath("//a[@href]='inputs'"));
        inputButton.click();
    }
}
