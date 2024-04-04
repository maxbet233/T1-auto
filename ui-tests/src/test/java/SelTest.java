import com.codeborne.selenide.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.codeborne.selenide.Condition.*;
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

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    @DisplayName("Number option:")
    void dropDown(int optionNumber){
        SelenideElement link = $x("//a[@href='/dropdown']");
        link.should(visible).click();

        SelenideElement select = $x("//select[@id='dropdown']");
        select.click();

        ElementsCollection options = $$x("//option[@value]");
        options.get(optionNumber).click();
        options.get(optionNumber).should(visible);
    }

    @RepeatedTest(value = 10, name = "Поиск 5 элементов")
    void disappearingElements(){
        SelenideElement link = $x("//a[@href='/disappearing_elements']");
        link.should(visible).click();

        ElementsCollection countLinks = $$x("//li");
        countLinks.should(CollectionCondition.size(5));
    }

    @TestFactory
    @DisplayName("Проверка введенных значений")
    List<DynamicTest> testSendKeys() {
        List<DynamicTest> res = new ArrayList<>();
        List<String> testData = randomInput();

        SelenideElement link = $x("//a[@href='/inputs']");
        link.should(visible).click();

        for (int i = 0; i < testData.size(); i++) {
            final int index = i;

            res.add(
                    DynamicTest.dynamicTest(
                            "Test " + i,
                            () -> {
                                SelenideElement input = $x("//input[@type = 'number']");
                                input.sendKeys(testData.get(index));
                                input.should(Condition.value(testData.get(index)));
                                refresh();
                            }
                    )
            );
        }
        return res;
    }

    private List<String> randomInput() {
        List<String> result = new ArrayList<>();
        result.add("123");
        result.add("456456");
        result.add("3235432452");
        result.add("123323588967432452");
        result.add("632332452");
        result.add("0323533432452");
        result.add("32354456432452");
        result.add("32354564532452");
        result.add("456456");
        result.add("35456;$#");
        return result;
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    @DisplayName("Number hover:")
    void hovers(int numHover){
        SelenideElement link = $x("//a[@href='/hovers']");
        link.should(visible).click();
        ElementsCollection img = $$x("//div[@class='figure']");
        img.get(numHover).hover().should(have(text("name: user" + (numHover+1))));
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