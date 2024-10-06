package com.jmunoz.restmvc.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;

// Este test es un @SpringBootTest, en concreto una web application, porque RestAssured
// necesita que el application server esté ejecutándose.
// Con RANDOM_PORT, esto debería ejecutarse en un CI build server.
// No sabemos si el puerto 8080 o 8081 va a estar disponible en tiempo de testing.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BeerControllerRestAssuredTest {

    // Determinando que PORT es.
    // Cuando este test se ejecute, SpringBoot va a asignar un puerto y lo enlaza.
    @LocalServerPort
    Integer localPort;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = localPort;
    }

    // En este caso no tenemos una forma conveniente de obtener un token JWT de autorización.
    // Eso hace que el test falle, ya que el endpoint está securizado.
    // Tenemos que deshabilitar Spring Security para estos tests.
    @Test
    void testListBeers() {
        given().contentType(ContentType.JSON)
                .when()
                .get("/api/v1/beer")
                .then()
                .assertThat().statusCode(200);
    }
}
