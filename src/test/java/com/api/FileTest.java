package com.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;

public class FileTest {
    
    @Test
    public void deveObrigarEnvioArquivo(){

        given()
            .log().all()
        .when()
            .post("http://restapi.wcaquino.me/upload")
        .then()
            .log().all()
            .statusCode(400)
            .body("error", is("Arquivo não enviado"))
        ;
    }

    @Test
    public void deveFazerUploadeArquivo(){

        given()
            .log().all()
            .multiPart("arquivo", new File("src/main/java/com/api/resources/arquivoupload.pdf"))
        .when()
            .post("http://restapi.wcaquino.me/upload")
        .then()
            .log().all()
            .statusCode(200)
            .body("name", is("arquivoupload.pdf"))        
        ;
    }

    @Test
    public void naoDeveFazerUploadeArquivoGrande(){

        given()
            .log().all()
            .multiPart("arquivo", new File("src/main/java/com/api/resources/itext-2.1.7.jar"))
        .when()
            .post("http://restapi.wcaquino.me/upload")
        .then()
            .log().all()
            .time(lessThan(10000L))
            .statusCode(413)      
        ;
    }

    @Test
    public void deveBaixarArquivo() throws IOException{
        byte[] image = given()
            .log().all()
        .when()
            .get("http://restapi.wcaquino.me/download")
        .then()
            .log().all()
            .statusCode(200)   
            .extract().asByteArray()   
        ;
        File imagem = new File("src/main/java/com/api/resources/file.jpg");
        OutputStream out = new FileOutputStream(imagem);
        out.write(image);
        out.close();

        MatcherAssert.assertThat(imagem.length(), lessThan(100000L));
    }
}
