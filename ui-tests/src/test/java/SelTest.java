import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.GeckoDriverInfo;

import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class SelTest {

    WebDriver driver;
    @BeforeEach
    void setup(){
        Configuration.browser = "firefox";
        open("https://the-internet.herokuapp.com/");
    }

    @Test
    void checkBoxes(){
        SelenideElement link = $x("//a[@href='/checkboxes']");
        link.should(visible).click();
        ElementsCollection checkBoxes = $$x("//input[@type='checkbox']");
        checkBoxes.get(0).click();
        System.out.println(checkBoxes.get(0).isSelected());
        checkBoxes.get(1).click();
        System.out.println(checkBoxes.get(1).isSelected());
    }

    @Test
    void dropDown(){
        SelenideElement link = $x("//a[@href='/dropdown']");
        link.should(visible).click();
        SelenideElement select = $x("//select[@id='dropdown']");
        select.click();
        ElementsCollection options = $$x("//option[@value]");
        options.get(1).click();
        System.out.println(options.get(1).text());
        select.click();
        options.get(2).click();
        System.out.println(options.get(2).text());

    }

    @AfterEach
    void tearDown(){
        closeWindow();
    }
}