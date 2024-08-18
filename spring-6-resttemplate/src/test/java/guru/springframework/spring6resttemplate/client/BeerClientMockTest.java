package guru.springframework.spring6resttemplate.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6resttemplate.config.RestTemplateBuilderConfig;
import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

// La dificultad de hacer testing a RestTemplate es que por debajo se está usando el cliente RestTemplate real,
// por lo que, por defecto, va a intentar conectarse a un servidor real.
// Y, para testear, necesitamos un endpoint de prueba.
// Lo que podemos hacer es usar un mock de un cliente, igual que hacíamos con Spring Mock MVC, donde podemos crear
// un mock de un servidor con el que trabajar. Esto conlleva cierta configuración.
//
// Para evitar el fallo del test, quitamos el enlace a la clase e importamos nuestra configuración de RestTemplate.
// @RestClientTest(BeerClientImpl.class)
@RestClientTest
@Import(RestTemplateBuilderConfig.class)
public class BeerClientMockTest {

    static final String URL = "http://localhost:8080";

    // Para evitar el fallo del test, no necesitamos el client del contexto.
    // @Autowired
    BeerClient beerClient;

    // Para evitar el fallo del test, no obtenemos el server del contexto.
    // Lo inicializamos en el méto-do setUp.
    // @Autowired
    MockRestServiceServer server;

    // Y necesitamos nuestra configuración de RestTemplateBuilder
    @Autowired
    RestTemplateBuilder restTemplateBuilderConfigured;

    // SpringBoot por defecto crea una instancia de Jackson ObjectMapper.
    @Autowired
    ObjectMapper objectMapper;

    // Creamos un template builder especializado.
    @Mock
    RestTemplateBuilder mockRestTemplateBuilder = new RestTemplateBuilder(new MockServerRestTemplateCustomizer());

    BeerDTO beerDto;
    String dtoJson;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        // Creamos una instancia de RestTemplate usando nuestro builder por defecto.
        // Este es el RestTemplate object que ha sido enlazado al server.
        RestTemplate restTemplate = restTemplateBuilderConfigured.build();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        when(mockRestTemplateBuilder.build()).thenReturn(restTemplate);

        // Inyectamos a la implementación de BeerClient nuestro template builder.
        beerClient = new BeerClientImpl(mockRestTemplateBuilder);

        // Obtenemos un BeerDTO con un id concreto.
        beerDto = getBeerDto();

        // Configuramos el mock para devolver esta respuesta JSON.
        dtoJson = objectMapper.writeValueAsString(beerDto);
    }

    // Este test nos falla con el siguiente error:
    // Unable to use auto-configured MockRestServiceServer since a mock server customizer has not
    // been bound to a RestTemplate or RestClient.
    //
    // El problema es que estamos usando RestTemplateBuilder dentro de la clase y estamos creando un nuevo Rest Template,
    // y este Rest Template no queda enlazado al server.
    //
    // Lo que vamos a hacer es que el RestTemplate quede enlazado al mock server y usaremos Mockito para crear
    // un mock que devolverá el template del méto-do builder.
    // Es decir, vamos a imitar el comportamiento que queremos para testear nuestro rest client, proporcionándole
    // un RestTemplate debidamente configurado que queda enlazado al mock server usando Mockito.
    //
    // El test ya funciona.
    @Test
    void testListBeers() throws JsonProcessingException {

        // Configuramos el mock para devolver el payload JSON.
        String payload = objectMapper.writeValueAsString(getPage());

        // Configuramos la interacción con el mock
        server.expect(method(HttpMethod.GET))
                .andExpect(requestTo(URL + BeerClientImpl.GET_BEER_PATH))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

        Page<BeerDTO> dtos = beerClient.listBeers();

        assertThat(dtos.getContent().size()).isGreaterThan(0);
    }

    @Test
    void testGetBeerById() {

        // Configuramos la interacción con el mock
        mockGetOperation();

        BeerDTO responseDto = beerClient.getBeerById(beerDto.getId());

        assertThat(responseDto.getId()).isEqualTo(beerDto.getId());
    }

    @Test
    void testCreateBeer() {

        // Para la parte del POST.
        // Notar el andRespond, donde indicamos que hay un location correcto (en el Body no viene el objeto creado)
        URI uri = UriComponentsBuilder.fromPath(BeerClientImpl.GET_BEER_BY_ID_PATH)
                        .build(beerDto.getId());

        server.expect(method(HttpMethod.POST))
                .andExpect(requestTo(URL + BeerClientImpl.GET_BEER_PATH))
                .andRespond(withAccepted().location(uri));

        // Para la parte del GET (en el body no venía el objeto creado)
        mockGetOperation();

        BeerDTO responseDto = beerClient.createBeer(beerDto);

        assertThat(responseDto.getId()).isEqualTo(beerDto.getId());
    }

    @Test
    void testUpdateBeer() {

        server.expect(method(HttpMethod.PUT))
                .andExpect(requestToUriTemplate(URL + BeerClientImpl.GET_BEER_BY_ID_PATH, beerDto.getId()))
                .andRespond(withNoContent());

        mockGetOperation();

        BeerDTO responseDto = beerClient.updateBeer(beerDto);
        assertThat(responseDto.getId()).isEqualTo(beerDto.getId());
    }

    @Test
    void testDeleteBeer() {

        server.expect(method(HttpMethod.DELETE))
                .andExpect(requestToUriTemplate(URL + BeerClientImpl.GET_BEER_BY_ID_PATH, beerDto.getId()))
                .andRespond(withNoContent());

        beerClient.deleteBeer(beerDto.getId());

        // Como delete no devuelve nada, solo verificamos que se ha realizado la llamada al mock.
        server.verify();
    }

    private void mockGetOperation() {
        server.expect(method(HttpMethod.GET))
                .andExpect(requestToUriTemplate(URL + BeerClientImpl.GET_BEER_BY_ID_PATH, beerDto.getId()))
                .andRespond(withSuccess(dtoJson, MediaType.APPLICATION_JSON));
    }

    BeerDTO getBeerDto() {
        return BeerDTO.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123245")
                .build();
    }

    BeerDTOPageImpl<BeerDTO> getPage() {
        return new BeerDTOPageImpl<>(Collections.singletonList(getBeerDto()), 1, 25, 1);
    }
}
