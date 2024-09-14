package com.jmunoz.spring6datar2dbc.controllers;

import com.jmunoz.spring6datar2dbc.model.BeerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

// Recordar que es un controlador reactivo no podemos usar MockMVC porque no existe un contexto de servlet.
// Tenemos que usar WebTestClient, que es reactivo.
// Para ello usamos la anotación @AutoConfigureWebTestClient
//
// Usamos el contexto completo de SpringBoot para así obtener la BD H2 en memoria y nuestra data cargada (bootstrap)
@SpringBootTest
@AutoConfigureWebTestClient
class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testListBeers() {

        // Decimos: Haz una operación GET contra la URI indicada y vamos a hacer un exchange, a partir del cual
        // vamos a esperar un status OK, un header con contenido JSON, y en su jsonPath indicamos que el array
        // debe contener 3 objetos en el.
        webTestClient.get().uri(BeerController.BEER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    void testGetById() {
        webTestClient.get().uri(BeerController.BEER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(BeerDTO.class);

    }
}