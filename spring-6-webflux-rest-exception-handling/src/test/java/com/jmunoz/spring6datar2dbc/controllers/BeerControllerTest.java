package com.jmunoz.spring6datar2dbc.controllers;

import com.jmunoz.spring6datar2dbc.domain.Beer;
import com.jmunoz.spring6datar2dbc.model.BeerDTO;
import com.jmunoz.spring6datar2dbc.repositories.BeerRepositoryTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

// Recordar que es un controlador reactivo no podemos usar MockMVC porque no existe un contexto de servlet.
// Tenemos que usar WebTestClient, que es reactivo.
// Para ello usamos la anotación @AutoConfigureWebTestClient
//
// Usamos el contexto completo de SpringBoot para así obtener la BD H2 en memoria y nuestra data cargada (bootstrap)
//
// Añadimos ordenación a la ejecución de los tests, porque si se ejecuta primero el que elimina una beer, resulta
// que no funciona el test que actualiza. Por lo tanto, el orden de la ejecución de los tests sí que nos importa.
// To-do esto ocurre porque, a diferencia de los tests de integración de Spring Boot, no se hace un rollback automático.
// Para ello, indicamos la anotación @TestMethodOrder(MethodOrderer.OrderAnnotation.class) y luego, en los métdoos que
// deseemos, indicamos su @Order()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class BeerControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @Order(2)
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
    @Order(1)
    void testGetById() {
        webTestClient.get().uri(BeerController.BEER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(BeerDTO.class);

    }

    @Test
    void testGetByIdNotFound() {
        webTestClient.get().uri(BeerController.BEER_PATH_ID, 999)
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void testCreateBeer() {
        // El location lo hemos obtenido haciendo que falle el test. Es decir, he puesto
        // .expectHeader().location("asdf"), ha fallado el test y me ha dado el location correcto, que
        // he puesto en sustitución de "asdf"
        webTestClient.post().uri(BeerController.BEER_PATH)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/beer/4");
    }

    @Test
    void testCreateBeerBadData() {
        Beer testBeer = BeerRepositoryTest.getTestBeer();
        testBeer.setBeerName("");

        webTestClient.post().uri(BeerController.BEER_PATH)
                .body(Mono.just(testBeer), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(3)
    void testUpdateBeer() {
        // Para el test no nos importa qué se está actualizando, pero sí que la actualización ocurre.
        webTestClient.put().uri(BeerController.BEER_PATH_ID, 1)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDTO.class)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(4)
    void testUpdateBeerBadRequest() {
        Beer testBeer = BeerRepositoryTest.getTestBeer();
        testBeer.setBeerStyle("");

        webTestClient.put().uri(BeerController.BEER_PATH_ID, 1)
                .body(Mono.just(testBeer), BeerDTO.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testUpdateBeerNotFound() {
        webTestClient.put().uri(BeerController.BEER_PATH_ID, 999)
                .body(Mono.just(BeerRepositoryTest.getTestBeer()), BeerDTO.class)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(999)
    void testDeleteBeer() {
        webTestClient.delete().uri(BeerController.BEER_PATH_ID, 1)
                .exchange()
                .expectStatus().isNoContent();
    }
}