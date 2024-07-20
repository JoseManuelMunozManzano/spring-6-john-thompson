package com.jmunoz.restmvc.controller;

import com.jmunoz.restmvc.model.Beer;
import com.jmunoz.restmvc.model.BeerStyle;
import com.jmunoz.restmvc.services.BeerService;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Esta anotación indica que es un test splice, y queremos limitarlo a la clase BeerController.
@WebMvcTest(BeerController.class)
class BeerControllerTest {

    // Esto configura Spring MockMvc
    @Autowired
    MockMvc mockMvc;

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
    // NOTA: Como se indica más abajo, es mejor crear un objeto concreto de prueba de Beer.

    @Test
    void getBeerById() throws Exception {
        // Podemos usar la implementación para obtener un objeto de tipo Beer
        // Beer testBeer = beerServiceImpl.listBeers().get(0);
        //
        // O mejor, crearnos un objeto y no tener que usar nuestra implementación del servicio.
        Beer testBeer = Beer.builder()
                .id(UUID.randomUUID())
                .beerName("My Beer Brand")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("beerbeer")
                .price(new BigDecimal("12.99"))
                .version(1)
                .quantityOnHand(29)
                .build();

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

        given(beerService.listBeers()).willReturn(List.of(beer1, beer2, beer3));

        // En este caso hacemos aserciones sobre la lista, en concreto su longitud.
        mockMvc.perform(get("/api/v1/beer")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }
}