import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Random;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class SelTest {
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
    @Test
    void disappearingElements(){
        SelenideElement link = $x("//a[@href='/disappearing_elements']");
        link.should(visible).click();

        for (int i = 1; i <= 10; i++){
            ElementsCollection countLinks = $$x("//li");
            if (countLinks.size() == 5){
                System.out.println("Найдено " + countLinks.size() + " элементов. Количество попыток " + i);
                break;
            }
            else if (i == 10){
                System.out.println("Не найдено за " + i + "попыток");
                assert countLinks.size() == 5;
                break;
            }
            else if (countLinks.size() != 5){
                refresh();
            }
        }
    }
    @Test
    void randomInput(){
        Random rand = new Random();
        int x = rand.nextInt(10000);
        SelenideElement link = $x("//a[@href='/inputs']");
        link.should(visible).click();
        SelenideElement input = $x("//input[@type = 'number']");
        input.sendKeys("" + x);
        System.out.println(input.val());
    }
    @Test
    void hovers() throws InterruptedException {
        Random rand = new Random();
        int x = rand.nextInt(10000);
        SelenideElement link = $x("//a[@href='/inputs']");
        link.should(visible).click();
        SelenideElement input = $x("//input[@type = 'number']");
        input.sendKeys("" + x);
        System.out.println(input.val());
        Thread.sleep(3000);
    }

    @AfterEach
    void tearDown(){
        closeWindow();
    }

}