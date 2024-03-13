import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class RestAssuredTest {
    private String product = new StringBuilder()
            .append("{")
            .append("\"name\": \"Java\",")
            .append("\"category\": \"Lang\",")
            .append("\"price\": \"45\",")
            .append("\"discount\": \"3\",")
            .append("}").toString();

    private String cart = new StringBuilder()
            .append("{")
            .append("\"product_id\": \"1\",")
            .append("\"quantity\": \"8\"")
            .append("}").toString();

    private static Integer productId = 1;
    private static String new_token;
    private static String new_credentionals;
    private static String login;


    @BeforeAll
    public static void beforeAll() throws FileNotFoundException {
        RestAssured.baseURI = "http://9b142cdd34e.vps.myjino.ru:49268";

        PrintStream print = new PrintStream("log.txt");
        RestAssured.filters(RequestLoggingFilter.logRequestTo(System.out), ResponseLoggingFilter.logResponseTo(print));

        new_credentionals = new StringBuilder()
                .append("{")
                .append("\"username\": \"DoctorJerrie310\",")
                .append("\"password\": \"pass123\"")
                .append("}").toString();

        new_token = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(new_credentionals)
                .post("/login")
                .then().assertThat().statusCode(200)
                .and().extract().body().jsonPath().getString("access_token");
    }



    @Test
    public void newRegister(){
        Faker faker = new Faker();
        login = faker.superhero().prefix()+faker.name().firstName()+faker.address().buildingNumber();
        new_credentionals = new StringBuilder()
                .append("{")
                .append("\"username\": \"" + login + "\",")
                .append("\"password\": \"pass123\"")
                .append("}").toString();
        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(new_credentionals)
                .post("/register");
        response.then().statusCode(201);
    }

    @Test
    public void LogIn(){
        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(new_credentionals)
                .post("/login");
        response.then().statusCode(200);
    }

    @Test
    public void getProduct(){
        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .get("/products");
        response.then().statusCode(200);
    }

    @Test
    public void newProduct(){
        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(product)
                .post("/products");
        response.then().statusCode(201);
    }

    @Test
    public void getProductId(){
        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .get("/products/" + productId);
        response.then().statusCode(200);
    }

    @Test
    public void editProduct(){
        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(product)
                .put("/products/" + productId);
        response.then().statusCode(200);
    }

    @Test
    public void delProduct(){
        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .delete("/products/" + productId);
        response.then().statusCode(200);
    }

    @Test
    public void newCart(){
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + new_token)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(cart)
                .post("/cart");
        response.then().statusCode(201);
    }

    @Test
    public void getCart(){
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + new_token)
                .accept(ContentType.JSON)
                .get("/cart");
        response.then().statusCode(200);
    }

    @Test
    public void delProductOfCart(){
        Response response = RestAssured.given()
                .header("Authorization", "Bearer " + new_token)
                .delete("/cart/" + productId);
        response.then().statusCode(200);
    }

}
