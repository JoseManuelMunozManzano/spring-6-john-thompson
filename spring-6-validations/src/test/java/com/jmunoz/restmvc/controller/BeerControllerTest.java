package com.jmunoz.restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmunoz.restmvc.model.BeerDto;
import com.jmunoz.restmvc.model.BeerStyle;
import com.jmunoz.restmvc.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    List<BeerDto> beers;

    @BeforeEach
    void setUp() {
        BeerDto beer1 = BeerDto.builder()
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

        BeerDto beer2 = BeerDto.builder()
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

        BeerDto beer3 = BeerDto.builder()
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

    // Lanzamiento de excepción usando Mockito.
    // En concreto vamos a probar la excepción 404 - Not Found
    @Test
    void getBeerByIdNotFound() throws Exception {

        // Mockito debe devolver la excepción.
        // Este test genera una excepción y es correcto porque tenemos en el controller
        // el manejador de esta excepción handleNotFoundException()
        // (Además tenemos el manejador global, pero se usa el local del controller)
        //
        // Gracias a Optional hemos delegado el lanzamiento de excepción del service al controller.
        // Cambiamos de
        // given(beerService.getBeerById(any(UUID.class))).willThrow(NotFoundException.class);
        // a que el service devuelve empty.
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBeerById() throws Exception {
        // Podemos usar la implementación para obtener un objeto de tipo Beer
        // Beer testBeer = beerServiceImpl.listBeers().get(0);
        //
        // O mejor, crearnos un objeto y no tener que usar nuestra implementación del servicio.
        BeerDto testBeer = beers.getFirst();

        // Aquí decimos: dado el método beerService.getBeerById, si pasamos cualquier UUID nos va a devolver
        // el objeto testBeer.
        // given(beerService.getBeerById(any(UUID.class))).willReturn(testBeer);
        //
        // O podemos usar el id que se ha añadido a testBeer
        //
        // Haciendo uso del Optional de Java.
        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));

        // Aquí decimos: queremos hacer un get a esa URL y deberíamos obtener un status Ok y contenido JSON.
        // Sobre ese JSON hacemos aserciones.
        mockMvc.perform(get(BeerController.BEER_PATH_ID, testBeer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    // Test de validación
    @Test
    void testCreateBeerNullBeerName() throws Exception {
        BeerDto beerDto = BeerDto.builder().build();

        given(beerService.saveNewBeer(any(BeerDto.class))).willReturn(beers.getFirst());

        // Recuperando información del error devuelto por el controller. Notar el .andReturn()
        MvcResult mvcResult = mockMvc.perform(post(BeerController.BEER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerDto)))
                .andExpect(status().isBadRequest()).andReturn();

        // Con un debug en la línea siguiente he visto:
        // Veo que devuelve la excepción MethodArgumentNotValidException.class
        // Veo que me devuelve mirando mvcResult - mockResponse - content
        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testListBeers() throws Exception {

        given(beerService.listBeers()).willReturn(beers);

        // En este caso hacemos aserciones sobre la lista, en concreto su longitud.
        mockMvc.perform(get(BeerController.BEER_PATH)
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

        BeerDto beer = beers.getFirst();

        // Creando el JSON y verlo en consola.
        // System.out.println(objectMapper.writeValueAsString(beer));

        // Más adelante veremos DTO, por ahora usamos el Entity directamente.
        beer.setVersion(0);
        beer.setId(null);

        // Cogemos el beer(1) por el hecho de obtener un id cuando se hace un insert,
        // para no gastar tiempo en crear más data para el test.
        given(beerService.saveNewBeer(any(BeerDto.class))).willReturn(beers.get(1));

        // Aquí decimos: queremos hacer un post a esa URL con un JSON (beer) y debemos obtener
        // status de CREATED y en el header una property Location.
        mockMvc.perform(post(BeerController.BEER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testUpdateBeer() throws Exception {
        BeerDto beer = beers.getFirst();

        // No hace falta que hagamos la actualización, pero sí devolver un valor.
        given(beerService.updateBeerById(any(), any())).willReturn(Optional.of(beer));

        // Usando Mockito, vamos a verificar la interacción, es decir, que el service fue llamado.
        mockMvc.perform(put(BeerController.BEER_PATH_ID, beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());

        // Por defecto, verifica una interacción.
        verify(beerService).updateBeerById(eq(beer.getId()), any(BeerDto.class));
    }

    @Test
    void testDeleteBeer() throws Exception {
        BeerDto beer = beers.getFirst();

        // Delete devuelve una bandera. Si existe id se hace el delete y devuelve true, y si no existe id devuelve false.
        given(beerService.deleteBeerById(any())).willReturn(true);

        mockMvc.perform(delete(BeerController.BEER_PATH_ID, beer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Usando ArgumentCaptor
        // Se usa para capturar los argumentos pasados a un método mock, para poder luego inspeccionarlo y realizar aserciones.
        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);

        verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());

        // Nos aseguramos que el UUID pasado al controlador a través del PathVariable se ha parseado correctamente.
        // Esto se usa mucho para verificar que las propiedades se envían correctamente.
        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testPatchBeer() throws Exception {
        BeerDto beer = beers.getFirst();

        // No hace falta que hagamos la actualización, pero sí devolver un valor.
        given(beerService.patchBeerById(any(), any())).willReturn(Optional.of(beer));

        // Obtenemos un JSON object con solo el nombre de la cerveza, que imita lo que el cliente haría cuando
        // usa la operación PATCH.
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name");

        mockMvc.perform(patch(BeerController.BEER_PATH_ID, beer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        // En vez de usar @Captor, aquí sigo usando la inicialización manual por temas pedagógicos, para ver
        // las dos posibilidades.
        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<BeerDto> beerArgumentCaptor = ArgumentCaptor.forClass(BeerDto.class);

        verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
    }
}