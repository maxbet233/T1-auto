package org.example;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class RestAssuredTest {

    private static final String login = "GreenMiles711";
    private static String new_login = "";
    private static final String password = "string2";
    private static final String register_endpoint = "/register";
    private static final String login_endpoint = "/login";
    private static final String product_endpoint = "/products";
    private static final String cart_endpoint = "/cart";
    private static final String Path = "src/test/resources";

    private String cart = new StringBuilder()
            .append("{")
            .append("\"product_id\": \"1\",")
            .append("\"quantity\": \"8\"")
            .append("}").toString();


    @BeforeAll
    public static void beforeAll() throws FileNotFoundException {
        RestAssured.baseURI = "http://9b142cdd34e.vps.myjino.ru:49268";
        PrintStream print = new PrintStream("log.txt");
        RestAssured.filters(RequestLoggingFilter.logRequestTo(System.out), ResponseLoggingFilter.logResponseTo(print));
    }

    private void register(String login, String password) {
        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(new User(login, password))
                .post(register_endpoint);
    }

    private static String autorizate(String login, String password){
        return RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(new User(login, password))
                .when()
                .post(login_endpoint)
                .then().extract().as(Token.class).getAccessToken();
    }

    @Test
    public void registerTest() {
        Faker faker = new Faker();
        new_login = faker.superhero().prefix()+faker.name().firstName()+faker.address().buildingNumber();
        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(new User(new_login, password))
                .post(register_endpoint)
                .then().statusCode(201);
    }
    @Test
    public void logInTest() {
        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(new User(login, password))
                .post(login_endpoint)
                .then().statusCode(200);
    }

    @Test
    public void getProductTest() {
            var userlist = List.of (RestAssured.given()
                .accept(ContentType.JSON)
                .get(product_endpoint)
                .then().assertThat().statusCode(200)
                    .extract()
                    .body()
                    .as(ProductCard[].class));

            assert userlist.get(0).category().matches("\\D+");

        //ObjectMapper mapper = new ObjectMapper();
        //mapper.writeValue(new File(Path + "/productCard.json"), response.body());
    }

    @Test
    public void newCartTest(){
        String token = autorizate(login, password);
        RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token))
                .body(new AddProduct(1, 15))
                .post(cart_endpoint)
                .then().statusCode(201);
    }


}
