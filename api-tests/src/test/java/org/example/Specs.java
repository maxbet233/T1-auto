package org.example;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Specs {
    public static String login = "tester123";
    public static String password = "pass123";
    public static String name_product = "Car";
    public static String category = "Electronics";
    public static Float price = 15.5F;
    public static Float discount = 5.3F;
    public static String Path = "src/test/resources";
    public static RequestSpecification requestSpec(String baseUri, String basePath){
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setBasePath(basePath)
                .setContentType(ContentType.JSON)
                .build();
    }
    public static void installSpec(RequestSpecification request){
        RestAssured.requestSpecification = request;
    }

}
