package org.example;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RestAssuredTest {

    @BeforeAll
    public static void beforeAll() throws FileNotFoundException {
        PrintStream print = new PrintStream("log.txt");
        RestAssured.filters(RequestLoggingFilter.logRequestTo(System.out), ResponseLoggingFilter.logResponseTo(print));
    }

    private void register(String login, String password) {
        Specs.installSpec(Specs.requestSpec(Specs.uri, "register"));
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .body(new User(Specs.login, Specs.password))
                .post();
    }

    private static String autorizate(String login, String password){
        Specs.installSpec(Specs.requestSpec(Specs.uri, "login"));
        return RestAssured
                .given()
                .accept(ContentType.JSON)
                .body(new User(Specs.login, Specs.password))
                .when()
                .post()
                .then()
                .extract()
                .as(Token.class)
                .getAccessToken();
    }

    @Test
    public void logInTest() {
        Specs.installSpec(Specs.requestSpec(Specs.uri, "login"));
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .body(new User(Specs.login, Specs.password))
                .post()
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void getProductTest() {
        Specs.installSpec(Specs.requestSpec(Specs.uri, "products"));
        RestAssured
                .given()
                .get()
                .then()
                .assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("productCardSchema.json"));

    }
    @Test
    public void addNewProductTest() {
        Specs.installSpec(Specs.requestSpec(Specs.uri, "products"));
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .body(new AddProduct(Specs.name_product, Specs.category, Specs.price, Specs.discount))
                .post()
                .then()
                .assertThat()
                .statusCode(200);
    }
    @Test
    public void getProductIdTest() {
        Specs.installSpec(Specs.requestSpec(Specs.uri, "products"));
        RestAssured
                .given()
                .get("1")
                .then()
                .assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("productCardSchema.json"));
    }
    @Test
    public void getFakeProductIdTest() {
        Specs.installSpec(Specs.requestSpec(Specs.uri, "products"));
        RestAssured
                .given()
                .get("1rgtdf")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void putProductTest() {
        Specs.installSpec(Specs.requestSpec(Specs.uri, "products"));
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .body(new AddProduct(Specs.name_product, Specs.category, Specs.price, 88.34F))
                .post("1")
                .then()
                .assertThat()
                .statusCode(200);
    }
    @Test
    public void putFakeProductTest() {
        Specs.installSpec(Specs.requestSpec(Specs.uri, "products"));
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .body(new AddProduct(Specs.name_product, Specs.category, Specs.price, 88.34F))
                .put("1drfge45")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void delProductTest() {
        Specs.installSpec(Specs.requestSpec(Specs.uri, "products"));
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .put("1")
                .then().statusCode(200);
    }
    @Test
    public void delFakeProductTest() {
        Specs.installSpec(Specs.requestSpec(Specs.uri, "products"));
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .post("1thrth")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void getCartTest(){
        String token = autorizate(Specs.login, Specs.password);
        Specs.installSpec(Specs.requestSpec(Specs.uri, "cart"));
        RestAssured
                .given()
                .header(new Header("Authorization", "Bearer " + token))
                .get()
                .then()
                .assertThat()
                .statusCode(200);
    }
    @Test
    public void getCartNoAuthTest(){
        Specs.installSpec(Specs.requestSpec(Specs.uri, "cart"));
        RestAssured
                .given()
                .get()
                .then()
                .assertThat()
                .statusCode(401);
    }

    @Test
    public void newCartTest(){
        String token = autorizate(Specs.login, Specs.password);
        Specs.installSpec(Specs.requestSpec(Specs.uri, "cart"));
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token))
                .body(new AddProductInCart(Specs.id, Specs.quantity))
                .post()
                .then()
                .assertThat()
                .statusCode(201);
    }
    @Test
    public void newCartNoAuthTest(){
        Specs.installSpec(Specs.requestSpec(Specs.uri, "cart"));
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .body(new AddProductInCart(1, 15))
                .post()
                .then()
                .assertThat()
                .statusCode(401);
    }
    @Test
    public void delProductOfCartTest(){
        String token = autorizate(Specs.login, Specs.password);
        Specs.installSpec(Specs.requestSpec(Specs.uri, "cart"));
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token))
                .delete("1")
                .then()
                .assertThat()
                .statusCode(200);
    }
    @Test
    public void delFakeProductOfCartTest(){
        String token = autorizate(Specs.login, Specs.password);
        Specs.installSpec(Specs.requestSpec(Specs.uri, "cart"));
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token))
                .delete("1fgd")
                .then()
                .assertThat()
                .statusCode(404);
    }
    @Test
    public void delProductOfCartNoAuthTest(){
        Specs.installSpec(Specs.requestSpec(Specs.uri, "cart"));
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .delete("1")
                .then()
                .assertThat()
                .statusCode(401);
    }
}
