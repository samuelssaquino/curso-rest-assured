package com.api;

import static io.restassured.RestAssured.given;

import org.junit.Test;

import io.restassured.matcher.RestAssuredMatchers;

public class SchemaTest {
 
    //nao funcionou
    // @Test
    // public void deveValidarSchemaXML(){
    //     given()
    //         .log().all()
    //     .when()
    //         .get("https://restapi.wcaquino.me/usersXML")
    //     .then()
    //         .log().all()
    //         .statusCode(200)
    //         .body(RestAssuredMatchers.matchesXsdInClasspath("src/main/java/com/api/resources/users.xsd"))
    //     ;
    // }
}
