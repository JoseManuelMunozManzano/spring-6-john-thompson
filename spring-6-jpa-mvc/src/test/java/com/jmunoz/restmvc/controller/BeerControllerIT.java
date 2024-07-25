package com.jmunoz.restmvc.controller;

import com.jmunoz.restmvc.entities.BeerEntity;
import com.jmunoz.restmvc.model.BeerDto;
import com.jmunoz.restmvc.repositories.BeerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

// En este test de integración hacemos tests a la interacción entre el service y el controller.
// Traeremos el contexto de Spring completo (@SpringBootTest) y permitiremos que Spring cree
// la instancia del controller y conecte y configure la BBDD en memoria H2.
// Además, al usar to-do el contexto de Spring, se incluye también la data de la clase BootstrapData,
// es decir, tenemos un conjunto de datos para ejecutar nuestros tests.
// Notar que se crea el test de integración para el controller.

@SpringBootTest
class BeerControllerIT {

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    // En otros tests anteriores, probábamos la interacción entre el controller y el framework
    // usando Mock MVC.
    // Ahora vamos a hacer test al controller y sus interacciones con la capa de datos JPA.
    // Para hacer esto, vamos a llamar directamente al controller, es decir, testeamos los métodos
    // del controller como si fuéramos el framework de Spring, pero no tratamos con el contexto web.

    @Test
    void testListBeers() {
        List<BeerDto> dtos = beerController.listBeers();

        assertThat(dtos.size()).isEqualTo(3);
    }

    // En los tests splices, Spring Boot automáticamente hace un rollback, es decir, se ejecuta en una transacción,
    // y se hace rollback, de forma implícita.
    // Pero como estamos en la capa de controller, hay que indicar a Spring Boot de forma explícita que queremos
    // tener transaccionalidad y queremos hacer rollback.
    // NOTA: La anotación @Transactional se puede importar de jakarta o de springframework
    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        beerRepository.deleteAll();
        List<BeerDto> dtos = beerController.listBeers();

        assertThat(dtos.size()).isEqualTo(0);
    }

    @Test
    void testGetById() {
        BeerEntity beer = beerRepository.findAll().getFirst();

        BeerDto dto = beerController.getBeerById(beer.getId());

        assertThat(dto).isNotNull();
    }
    
    // Testing de excepciones
    // Recordar que la excepción es capturada por el framework Spring MVC y la convierte en un error 404 NotFound.
    @Test
    void testBeerIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            // 99.999% de seguridad que genera un UUID distinto a los que tenemos en los datos de inicio (bootstrap)
            beerController.getBeerById(UUID.randomUUID());
        });
    }

    // Para que no fallen otros tests que calculen la cantidad de elementos, no olvidar hacer rollback del
    // elemento salvado.
    @Rollback
    @Transactional
    @Test
    void saveNewBeerTest() {
        // Emulamos lo que hace Spring MVC, es decir, toma un objeto JSON, lo parsea a un objeto BeerDto,
        // y Spring MVC va a llamar al método del controller con un DTO ya poblado.
        BeerDto beerDto = BeerDto.builder()
                .beerName("New Beer")
                .build();

        ResponseEntity<BeerDto> responseEntity = beerController.handlePost(beerDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString((locationUUID[1]));

        BeerEntity beerEntity = beerRepository.findById(savedUUID).get();
        assertThat(beerEntity).isNotNull();
    }
}