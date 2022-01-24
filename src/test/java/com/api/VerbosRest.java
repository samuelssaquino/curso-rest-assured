package com.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import io.restassured.http.ContentType;

public class VerbosRest {
    
    @Test
    public void deveSalvarUsuario(){
        given()
            .log().all()
            .contentType("application/json")
            .body("{\"name\": \"Samuel\", \"age\": 30}")
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .body("id", notNullValue())
            .body("name", is("Samuel"))
            .body("age", is(30))
            ;
    }

    @Test
    public void deveSalvarUsuarioUsandoMap(){

        Map<String, Object> params = new HashMap<String,Object>();
        params.put("name", "Usuario via map");
        params.put("age", 25);

        given()
            .log().all()
            .contentType("application/json")
            .body(params)
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .body("id", notNullValue())
            .body("name", is("Usuario via map"))
            .body("age", is(25))
            ;
    }

    @Test
    public void deveSalvarUsuarioUsandoObjeto(){

        User user = new User("Usuario via objeto", 35);

        given()
            .log().all()
            .contentType("application/json")
            .body(user)
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .body("id", notNullValue())
            .body("name", is("Usuario via objeto"))
            .body("age", is(35))
            ;
    }

    @Test
    public void deveDeserializarObjetoAoSalvarUsuario(){

        User user = new User("Usuario deserializado", 35);

        User usuarioInserido = given()
            .log().all()
            .contentType("application/json")
            .body(user)
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(201)
            .extract().body().as(User.class)
            ;

            System.out.println(usuarioInserido);
            assertThat(usuarioInserido.getId(), notNullValue());
            assertEquals("Usuario deserializado", usuarioInserido.getName());
            assertThat(usuarioInserido.getAge(), is(35));
    }

    @Test
    public void deveSalvarUsuarioViaXML(){
        given()
            .log().all()
            .contentType(ContentType.XML)
            .body("<user><name>Jose</name><age>50</age></user>")
        .when()
            .post("https://restapi.wcaquino.me/usersXML")
        .then()
            .log().all()
            .statusCode(201)
            .body("user.@id", notNullValue())
            .body("user.name", is("Jose"))
            .body("user.age", is("50"))
            ;
    }

    @Test
    public void deveSalvarUsuarioViaXMLUsandoObjeto(){
        User user = new User("Usuario XML", 40);

        given()
            .log().all()
            .contentType(ContentType.XML)
            .body(user)
        .when()
            .post("https://restapi.wcaquino.me/usersXML")
        .then()
            .log().all()
            .statusCode(201)
            .body("user.@id", notNullValue())
            .body("user.name", is("Usuario XML"))
            .body("user.age", is("40"))
            ;
    }
    @Test
    public void deveDeserializarXMLAoSalvarUsuario(){
        User user = new User("Usuario XML", 40);

        User usuarioInserido = given()
            .log().all()
            .contentType(ContentType.XML)
            .body(user)
        .when()
            .post("https://restapi.wcaquino.me/usersXML")
        .then()
            .log().all()
            .statusCode(201)
            .extract().body().as(User.class)
            ;

            System.out.println(usuarioInserido);
            assertThat(usuarioInserido.getId(), notNullValue());
            assertEquals("Usuario XML", usuarioInserido.getName());
            assertThat(usuarioInserido.getAge(), is(40));
            assertThat(usuarioInserido.getSalary(), is(0.0));
    }

    @Test
    public void naoDeveSalvarUsuarioSemNome(){
        given()
            .log().all()
            .contentType("application/json")
            .body("{\"age\": 30}")
        .when()
            .post("https://restapi.wcaquino.me/users")
        .then()
            .log().all()
            .statusCode(400)
            .body("id", nullValue())
            .body("error", is("Name é um atributo obrigatório"))          
            ;
    }
}