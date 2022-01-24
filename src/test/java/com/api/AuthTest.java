package com.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.XmlPath.CompatibilityMode;
import javafx.beans.binding.When;

public class AuthTest {

    @Test
    public void deveAcessarSWAPI(){

        given()
            .log().all()
        .when()
            .get("https://swapi.dev/api/people/1")
        .then()
            .log().all()
            .statusCode(200)
            .body("name", is("Luke Skywalker"))
        ;
    }

    @Test
    public void deveObterClima(){

        given()
            .log().all()
            .queryParam("q", "London,uk")
            .queryParam("appid", "36209ebb7e7acf08b7a48be3461a1376")//API Key
            .queryParam("units", "metric")
        .when()
            .get("http://api.openweathermap.org/data/2.5/weather")
        .then()
            .log().all()
            .statusCode(200)
            .body("name", is("London"))
            .body("coord.lon", is(-0.1257f))
            .body("main.temp", is(5.17f))
        ;
    }

    @Test
    public void naoDeveAcessarSemSenha(){

        given()
            .log().all()
        .when()
            .get("https://restapi.wcaquino.me/basicauth")
        .then()
            .log().all()
            .statusCode(401)              
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasica(){

        given()
            .log().all()
        .when()
            .get("https://admin:senha@restapi.wcaquino.me/basicauth")
        .then()
            .log().all()
            .statusCode(200)      
            .body("status", is("logado"))        
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasica2(){

        given()
            .log().all()
            .auth().basic("admin", "senha")
        .when()
            .get("https://restapi.wcaquino.me/basicauth")
        .then()
            .log().all()
            .statusCode(200)      
            .body("status", is("logado"))        
        ;
    }

    @Test
    public void deveFazerAutenticacaoBasicaChallenge(){

        given()
            .log().all()
            .auth().preemptive().basic("admin", "senha")
        .when()
            .get("https://restapi.wcaquino.me/basicauth2")
        .then()
            .log().all()
            .statusCode(200)      
            .body("status", is("logado"))        
        ;
    }

    @Test
    public void deveFazerAutenticacaoComTokenJWT(){
        Map<String, String> login = new HashMap<String, String>();
        login.put("email", "ts@ts.com");
        login.put("senha", "ts123456");

        String token = given()
            .log().all()
            .body(login)
            .contentType(ContentType.JSON)
        .when()
            .post("http://barrigarest.wcaquino.me/signin")
        .then()
            .log().all()
            .statusCode(200)          
            .extract().path("token")
        ;

        given()
            .log().all()
            .header("Authorization", "JWT " + token)
        .when()
            .get("http://barrigarest.wcaquino.me/contas")
        .then()
            .log().all()
            .statusCode(200)          
            .body("nome", Matchers.hasItem("Conta de Teste"))
            .body("nome", Matchers.hasItem("Contas de teste 2"))
            .body("nome", Matchers.hasItem("Contas de teste 3"))
        ;
    }

    @Test
    public void deveAcessarAplicacaoWeb(){
        
        String cookie = given()
            .log().all()
            .formParam("email", "ts@ts.com")
            .formParam("senha", "ts123456")
            .contentType(ContentType.URLENC.withCharset("UTF-8"))
        .when()
            .post("http://seubarriga.wcaquino.me/logar")
        .then()
            .log().all()
            .statusCode(200)
            .extract().header("set-cookie")
        ;

        cookie = cookie.split("=")[1].split(";")[0];
        System.out.println(cookie);

        String body = given()
            .log().all()
            .cookie("connect.sid", cookie)
        .when()
            .get("http://seubarriga.wcaquino.me/contas")
        .then()
            .log().all()
            .statusCode(200)
            .body("html.body.table.tbody.tr[0].td[0]", is("Conta de Teste"))
            .extract().body().asString()
        ;

        System.out.println("----------");

        XmlPath xmlPath = new XmlPath(CompatibilityMode.HTML, body);
        System.out.println(xmlPath.getString("html.body.table.tbody.tr[0].td[0]"));
    }
}
