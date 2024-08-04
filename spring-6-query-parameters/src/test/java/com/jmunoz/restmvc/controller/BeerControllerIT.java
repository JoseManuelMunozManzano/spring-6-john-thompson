package com.jmunoz.restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmunoz.restmvc.entities.BeerEntity;
import com.jmunoz.restmvc.mappers.BeerMapper;
import com.jmunoz.restmvc.model.BeerDto;
import com.jmunoz.restmvc.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Autowired
    BeerMapper beerMapper;

    // En otros tests anteriores, probábamos la interacción entre el controller y el framework
    // usando Mock MVC.
    // Ahora vamos a hacer test al controller y sus interacciones con la capa de datos JPA.
    // Para hacer esto, vamos a llamar directamente al controller, es decir, testeamos los métodos
    // del controller como si fuéramos el framework de Spring, pero no tratamos con el contexto web.
    //
    // Test Spring Boot completo.
    // Como se indica en el párrafo anterior, estamos trabajando directamente con el objeto controlador,
    // pero las violaciones a las validaciones de JPA van a burbujear de manera diferente, de la capa JPA
    // hacia el controlador.
    // Queremos hacer tests de las violaciones de constraints que vienen desde la BBDD en la capa JPA y
    // vamos a usar Mock MVC, pero de manera algo diferente, inyectando WebApplicationContext, donde
    // el nombre de la variable suele ser wac, y también se inyecta la propiedad MockMvc para el test,
    // y se inicializan en el método setup()
    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Vemos que inyectamos Spring Web Application Context a MockMvc. Esto configura el entorno Mock MVC
        // con el repository de Spring Data inyectado.
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void testListBeers() {
        List<BeerDto> dtos = beerController.listBeers();

        assertThat(dtos.size()).isEqualTo(2413);
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

    @Rollback
    @Transactional
    @Test
    void updateExistingBeer() {
        BeerEntity beerEntity = beerRepository.findAll().getFirst();
        BeerDto beerDto = beerMapper.beerEntityToBeerDto(beerEntity);
        beerDto.setId(null);
        beerDto.setVersion(null);
        final String beerName = "UPDATED";
        beerDto.setBeerName(beerName);

        ResponseEntity<BeerDto> responseEntity = beerController.updatedById(beerEntity.getId(), beerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        BeerEntity updatedBeer = beerRepository.findById(beerEntity.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> {
           beerController.updatedById(UUID.randomUUID(), BeerDto.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void deleteByIdFound() {
        BeerEntity beerEntity = beerRepository.findAll().getFirst();

        ResponseEntity<BeerDto> responseEntity = beerController.deleteById(beerEntity.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(beerRepository.findById(beerEntity.getId())).isEmpty();
    }

    @Test
    void testDeleteNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.deleteById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void patchExistingBeer() {
        BeerEntity beerEntity = beerRepository.findAll().getFirst();
        BeerDto beerDto = beerMapper.beerEntityToBeerDto(beerEntity);
        beerDto.setId(null);
        beerDto.setVersion(null);
        beerDto.setUpc(null);
        final String beerName = "PATCHED";
        beerDto.setBeerName(beerName);

        ResponseEntity<BeerDto> responseEntity = beerController.updateBeerPatchById(beerEntity.getId(), beerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        BeerEntity patchedBeer = beerRepository.findById(beerEntity.getId()).get();
        assertThat(patchedBeer.getBeerName()).isEqualTo(beerName);
    }

    @Test
    void testPatchNotFound() {
        assertThrows(NotFoundException.class, () -> {
            beerController.updateBeerPatchById(UUID.randomUUID(), BeerDto.builder().build());
        });
    }

    // Este es un test completo que falla con excepción TransactionSystemException, que es la excepción
    // que recoge todos los errores cuando algo va mal (así que hay otras excepciones por debajo),
    // debido a que falla la validación en la capa JPA.
    // Esto causa el rollback de la transacción y que burbujee hacia arriba.
    // Como nuestro controller no está manejando esta excepción de forma apropiada falla de esta forma
    // tan poco amistosa. Hay que manejar en el controlador esta excepción.
    @Test
    void testPatchBeerBadName() throws Exception {
        BeerEntity beer = beerRepository.findAll().getFirst();

        // Obtenemos un JSON object con solo el nombre de la cerveza, que imita lo que el cliente haría cuando
        // usa la operación PATCH.
        // Pero violamos la validación JPA de que el nombre no puede tener una longitud mayor de 50 caracteres.
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name 12345678901234567890123456789012345678901234567890123456789012345678901234567890");

        MvcResult result = mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        // Ponemos aquí un debug y vemos exception-cause
        // Vemos las excepciones
        // Genérica - TransactionSystemException
        // La que nos interesa - ConstraintViolationException
        // Y la última que se lanza con el rollback - RollbackException
        System.out.println(result.getResponse().getContentAsString());
    }

    // Tests en los que se usa Query Parameters para afectar el tamaño y número de registros a devolver.
    // Se hace un test de integración porque quiero que la BD esté levantada y con data (la proveniente del CSV)
    @Test
    void testListBeersByName() throws Exception {

        mockMvc.perform(get(BeerController.BEER_PATH)
                .queryParam("beerName", "IPA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(100)));
    }
}