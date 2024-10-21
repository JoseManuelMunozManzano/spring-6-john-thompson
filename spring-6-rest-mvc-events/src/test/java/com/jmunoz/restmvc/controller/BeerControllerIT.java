package com.jmunoz.restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmunoz.restmvc.entities.BeerEntity;
import com.jmunoz.restmvc.events.BeerCreatedEvent;
import com.jmunoz.restmvc.mappers.BeerMapper;
import com.jmunoz.restmvc.model.BeerDto;
import com.jmunoz.restmvc.model.BeerStyle;
import com.jmunoz.restmvc.repositories.BeerRepository;
import lombok.val;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// En este test de integración hacemos tests a la interacción entre el service y el controller.
// Traeremos el contexto de Spring completo (@SpringBootTest) y permitiremos que Spring cree
// la instancia del controller y conecte y configure la BBDD en memoria H2.
// Además, al usar to-do el contexto de Spring, se incluye también la data de la clase BootstrapData,
// es decir, tenemos un conjunto de datos para ejecutar nuestros tests.
// Notar que se crea el test de integración para el controller.

// Añadimos la anotación @RecordApplicationEvents para habilitar el soporte y saber si un evento se ha publicado.

@RecordApplicationEvents
@SpringBootTest
class BeerControllerIT {

    // Gracias a la anotación @RecordApplicationEvents tenemos disponible ApplicationEvents, que nos sirve
    // como un log de todos los eventos que se crean durante el test.
    @Autowired
    ApplicationEvents applicationEvents;

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
    // y se inicializan en el méto-do setup()
    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Vemos que inyectamos Spring Web Application Context a MockMvc. Esto configura el entorno Mock MVC
        // con el repository de Spring Data inyectado.
        //
        // Para que se aplique la seguridad es necesario incluir .apply(springSecurity()) exportándolo de
        // org.springframework.security.test.web.servlet.setup.
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    // Test para probar los application events.
    // Lo creamos así para obtener el contexto de autenticación de Spring, porque si llamamos al controlador
    // directamente, lo estaríamos subvirtiendo.
    // Así queda un test más realista, que crea un application event que incluye información del contexto
    // de seguridad de Spring.
    @Test
    void testCreateBeerMVC() throws Exception {
        val beerDTO = BeerDto.builder()
                .beerName("New Beer")
                .beerStyle(BeerStyle.IPA)
                .upc("123123")
                .price(BigDecimal.TEN)
                .quantityOnHand(5)
                .build();

        mockMvc.perform(post(BeerController.BEER_PATH)
                .with(BeerControllerTest.jwtRequestPostProcessor)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        // Obtención del evento publicado.
        // Al informar en el stream BeerCreatedEvents, se filtra en dicho stream solo lo de BeerCreatedEvents.
        // De esta forma, si hay más eventos, solo obtengo el que quiero.
        Assertions.assertEquals(1, applicationEvents
                .stream(BeerCreatedEvent.class)
                .count());
    }

    @Test
    void testListBeers() {
        Page<BeerDto> dtos = beerController.listBeers(null, null, false, 1, 2413);

        assertThat(dtos.getContent().size()).isEqualTo(1000);
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
        Page<BeerDto> dtos = beerController.listBeers(null, null, false, 1, 25);

        assertThat(dtos.getContent().size()).isEqualTo(0);
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
        // y Spring MVC va a llamar al méto-do del controller con un DTO ya poblado.
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
                        .with(BeerControllerTest.jwtRequestPostProcessor)
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
                        .with(BeerControllerTest.jwtRequestPostProcessor)
                .queryParam("beerName", "IPA")
                .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(336)));
    }

    @Test
    void testListBeersByStyle() throws Exception {

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(BeerControllerTest.jwtRequestPostProcessor)
                .queryParam("beerStyle", BeerStyle.IPA.name())
                .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(548)));
    }

    @Test
    void testListBeersByStyleAndName() throws Exception {

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(BeerControllerTest.jwtRequestPostProcessor)
                .queryParam("beerName", "IPA")
                .queryParam("beerStyle", BeerStyle.IPA.name())
                .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(310)));
    }

    @Test
    void testListBeersByStyleAndNameShowInventoryFalse() throws Exception {

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(BeerControllerTest.jwtRequestPostProcessor)
                .queryParam("beerName", "IPA")
                .queryParam("beerStyle", BeerStyle.IPA.name())
                .queryParam("showInventory", "FALSE")
                .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(310)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.nullValue()));
    }

    @Test
    void testListBeersByStyleAndNameShowInventoryTrue() throws Exception {

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(BeerControllerTest.jwtRequestPostProcessor)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "TRUE")
                        .queryParam("pageSize", "800"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(310)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    // Tests de paginación
    // Notar que pasamos como query parameters tanto pageNumber como pageSize.
    // Son 0 index.
    @Test
    void testListBeersByStyleAndNameShowInventoryTruePage2() throws Exception {

        mockMvc.perform(get(BeerController.BEER_PATH)
                        .with(BeerControllerTest.jwtRequestPostProcessor)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "TRUE")
                        .queryParam("pageNumber", "2")
                        .queryParam("pageSize", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(50)))
                .andExpect(jsonPath("$.content[0].quantityOnHand").value(IsNull.notNullValue()));
    }
    
    // Test para asegurarnos que funcione la autorización
    @Test
    void testNoAuth() throws Exception {

        mockMvc.perform(get(BeerController.BEER_PATH)
                .queryParam("beerStyle", BeerStyle.IPA.name())
                .queryParam("pageSize", "800"))
                .andExpect(status().isUnauthorized());
    }
}