package com.jmunoz.restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmunoz.restmvc.model.Beer;
import com.jmunoz.restmvc.model.BeerStyle;
import com.jmunoz.restmvc.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Esta anotación indica que es un test splice, y queremos limitarlo a la clase BeerController.
@WebMvcTest(BeerController.class)
class BeerControllerTest {

    // Esto configura Spring MockMvc
    @Autowired
    MockMvc mockMvc;

    // Usando el Object Mapper (Jackson) del context de Spring para serializar/deserializar JSON.
    // Obtenemos la configuración default de Spring Boot, y podemos configurarlo
    // de forma más avanzada.
    // Nuestros test y controladores van a usar la MISMA configuración, evitando conflictos y
    // evitando tener que mantener dos configuraciones distintas.
    @Autowired
    ObjectMapper objectMapper;

    // Esta anotación le dice a Mockito que provea un mock de este service al contexto de Spring.
    // Sin esta anotación obtendríamos una excepción indicando que no tenemos la dependencia, teniendo
    // que proveerla manualmente.
    // Por defecto, los mocks de Mockito devuelven una respuesta null.
    @MockBean
    BeerService beerService;

    // Para que los mocks devuelvan data, usaremos las implementaciones de nuestros services.
    // No utilizamos el método @Before porque no tenemos nada que inicializar. Si empezamos a modificar
    // data entre tests, entonces querremos inicializar la data.
    //
    // BeerServiceImpl beerServiceImpl = new BeerServiceImpl();
    //
    // NOTA: Como se indica más abajo, es mejor crear objetos concretos de prueba de Beer.
    List<Beer> beers;

    @BeforeEach
    void setUp() {
        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("1235622")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beers = List.of(beer1, beer2, beer3);
    }

    @Test
    void getBeerById() throws Exception {
        // Podemos usar la implementación para obtener un objeto de tipo Beer
        // Beer testBeer = beerServiceImpl.listBeers().get(0);
        //
        // O mejor, crearnos un objeto y no tener que usar nuestra implementación del servicio.
        Beer testBeer = beers.getFirst();

        // Aquí decimos: dado el método beerService.getBeerById, si pasamos cualquier UUID nos va a devolver
        // el objeto testBeer.
        // given(beerService.getBeerById(any(UUID.class))).willReturn(testBeer);
        //
        // O podemos usar el id que se ha añadido a testBeer
        given(beerService.getBeerById(testBeer.getId())).willReturn(testBeer);

        // Aquí decimos: queremos hacer un get a esa URL y deberíamos obtener un status Ok y contenido JSON.
        // Sobre ese JSON hacemos aserciones.
        mockMvc.perform(get("/api/v1/beer/" + testBeer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    @Test
    void testListBeers() throws Exception {

        given(beerService.listBeers()).willReturn(beers);

        // En este caso hacemos aserciones sobre la lista, en concreto su longitud.
        mockMvc.perform(get("/api/v1/beer")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    // Usando Jackson para crear un JSON y creación de Stub.
    @Test
    void testCreateNewBeer() throws Exception {
        // Jackson tiene lo que se llama ObjectMapper, que usaremos
        // para serializar/deserializar data de un JSON a un POJO o al revés.
        // Para que no falle Jackson, hay que configurar sus módulos, en concreto uno
        // para que maneje los tipos de fecha/hora.
        // Esta sería la declaración manual.
        //
        // ObjectMapper objectMapper = new ObjectMapper();
        // objectMapper.findAndRegisterModules();
        //
        // Pero se comenta porque lo mejor es inyectar del contexto de Spring ObjectMapper
        // ya configurado automáticamente por Spring (ver arriba su @Autowired)
        // Una de las diferencias con respecto a la declaración y configuración de arriba
        // es como quedan las fechas. Spring las formatea mejor.

        Beer beer = beers.getFirst();

        // Creando el JSON y verlo en consola.
        // System.out.println(objectMapper.writeValueAsString(beer));

        // Más adelante veremos DTO, por ahora usamos el Entity directamente.
        beer.setVersion(0);
        beer.setId(null);

        // Cogemos el beer(1) por el hecho de obtener un id cuando se hace un insert,
        // para no gastar tiempo en crear más data para el test.
        given(beerService.saveNewBeer(any(Beer.class))).willReturn(beers.get(1));

        // Aquí decimos: queremos hacer un post a esa URL con un JSON (beer) y debemos obtener
        // status de CREATED y en el header una property Location.
        mockMvc.perform(post("/api/v1/beer")
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }
}