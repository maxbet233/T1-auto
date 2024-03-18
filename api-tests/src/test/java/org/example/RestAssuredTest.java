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
        Specs.installSpec(Specs.requestSpec("http://9b142cdd34e.vps.myjino.ru:49268", "register"));
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(new User(Specs.login, Specs.password))
                .post();
    }

    private static String autorizate(String login, String password){
        Specs.installSpec(Specs.requestSpec("http://9b142cdd34e.vps.myjino.ru:49268", "login"));
        return RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
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
        Specs.installSpec(Specs.requestSpec("http://9b142cdd34e.vps.myjino.ru:49268", "login"));
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(new User(Specs.login, Specs.password))
                .post()
                .then()
                .statusCode(200);
    }

    @Test
    public void getProductTest() {
        Specs.installSpec(Specs.requestSpec("http://9b142cdd34e.vps.myjino.ru:49268", "products"));
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
        Specs.installSpec(Specs.requestSpec("http://9b142cdd34e.vps.myjino.ru:49268", "products"));
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(new AddProduct(Specs.name_product, Specs.category, Specs.price, Specs.discount))
                .post()
                .then().statusCode(200);
    }
    @Test
    public void getProductIdTest() {
        Specs.installSpec(Specs.requestSpec("http://9b142cdd34e.vps.myjino.ru:49268", "products"));
        RestAssured
                .given()
                .get("1")
                .then()
                .assertThat()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("productCardSchema.json"));

        RestAssured
                .given()
                .get("1rgtdf")
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void putProductTest() {
        Specs.installSpec(Specs.requestSpec("http://9b142cdd34e.vps.myjino.ru:49268", "products"));
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(new AddProduct(Specs.name_product, Specs.category, Specs.price, 88.34F))
                .post("1")
                .then().statusCode(200);
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(new AddProduct(Specs.name_product, Specs.category, Specs.price, 88.34F))
                .post("1drfge45")
                .then().statusCode(404);
    }

    @Test
    public void newCartTest(){
        String token = autorizate(Specs.login, Specs.password);
        Specs.installSpec(Specs.requestSpec("http://9b142cdd34e.vps.myjino.ru:49268", "cart"));
        RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer " + token))
                .body(new AddProductInCart(Specs.id, Specs.quantity))
                .post()
                .then().statusCode(201);

        RestAssured
                .given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(new AddProductInCart(1, 15))
                .post()
                .then().statusCode(401);
    }


}
