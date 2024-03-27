import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebElement;

import java.util.Random;

import static com.codeborne.selenide.Condition.checked;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class SelTest {
    @BeforeEach
    void setup(){
        Configuration.browser = "firefox";
        open("https://the-internet.herokuapp.com/");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 0})
    @DisplayName("Number check box:")
    void checkBoxes(int posNumber){
        SelenideElement link = $x("//a[@href='/checkboxes']");
        link.should(visible).click();
        ElementsCollection checkBoxes = $$x("//input[@type='checkbox']");

        checkBoxes.get(posNumber).click();
        checkBoxes.get(posNumber).should(Condition.attribute("checked", "true"));
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
    void hovers(){
        SelenideElement link = $x("//a[@href='/hovers']");
        link.should(visible).click();
        ElementsCollection img = $$x("//div[@class='figure']");

        System.out.println(img.get(0).hover().getText());
        System.out.println(img.get(1).hover().getText());
        System.out.println(img.get(2).hover().getText());
    }

    @Test
    void notificationMessage(){
        SelenideElement link = $x("//a[@href='/notification_message']");
        SelenideElement close = $x("//a[@class='close']");
        link.should(visible).click();
        close.click();

        while (!$x("//div[@class='flash notice']").getText().contains("Action successful")) {
            link.should(visible).click();
            close.click();
        }
    }

    @Test
    void addRemoveElements(){
        SelenideElement link = $x("//a[@href='/add_remove_elements/']");
        SelenideElement addButton = $x("//button[@onclick='addElement()']");
        ElementsCollection deleteElements = $$x("//button[@onclick='deleteElement()']");
        link.should(visible).click();
        Random rand = new Random();

        for (int i = 0; i < 5; i++){
            addButton.click();
            System.out.println(deleteElements.last().text());
        }

        for (int j = 0; j < 3; j++){
            int x = rand.nextInt(4-j);
            deleteElements.get(x).click();
            System.out.println(deleteElements.get(x).text());
        }
    }

    @Test
    void statusCode200(){
        SelenideElement link = $x("//a[@href='/status_codes']");
        link.should(visible).click();

        $x("//a[@href='status_codes/200']").click();
        SelenideElement page = $x("//body");
        System.out.println(page.text());
    }

    @Test
    void statusCode301(){
        SelenideElement link = $x("//a[@href='/status_codes']");
        link.should(visible).click();

        $x("//a[@href='status_codes/301']").click();
        SelenideElement page = $x("//body");
        System.out.println(page.text());
    }

    @Test
    void statusCode404(){
        SelenideElement link = $x("//a[@href='/status_codes']");
        link.should(visible).click();

        $x("//a[@href='status_codes/404']").click();
        SelenideElement page = $x("//body");
        System.out.println(page.text());
    }

    @Test
    void statusCode500(){
        SelenideElement link = $x("//a[@href='/status_codes']");
        link.should(visible).click();

        $x("//a[@href='status_codes/500']").click();
        SelenideElement page = $x("//body");
        System.out.println(page.text());
    }



    @AfterEach
    void tearDown(){
        closeWindow();
    }

}