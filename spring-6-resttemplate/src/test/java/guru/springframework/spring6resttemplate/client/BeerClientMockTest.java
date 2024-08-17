package guru.springframework.spring6resttemplate.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

// La dificultad de hacer testing a RestTemplate es que por debajo se está usando el cliente RestTemplate real,
// por lo que, por defecto, va a intentar conectarse a un servidor real.
// Y, para testear, necesitamos un endpoint de prueba.
// Lo que podemos hacer es usar un mock de un cliente, igual que hacíamos con Spring Mock MVC, donde podemos crear
// un mock de un servidor con el que trabajar. Esto conlleva cierta configuración.
@RestClientTest(BeerClientImpl.class)
public class BeerClientMockTest {

    static final String URL = "http://localhost:8080";

    @Autowired
    BeerClient beerClient;

    @Autowired
    MockRestServiceServer server;

    // SpringBoot por defecto crea una instancia de Jackson ObjectMapper.
    @Autowired
    ObjectMapper objectMapper;

    // Este test nos falla con el siguiente error:
    // Unable to use auto-configured MockRestServiceServer since a mock server customizer has not
    // been bound to a RestTemplate or RestClient.
    //
    // El problema es que estamos usando RestTemplateBuilder. RestTemplate es construido dentro de nuestro método
    // y quiere que lo enlacemos a el.
    //
    // Lo que vamos a hacer es que el RestTemplate quede enlazado al mock server y usaremos Mockito para crear
    // un mock que devolverá el template del méto-do builder.
    // Es decir, vamos a imitar el comportamiento que queremos para testear nuestro rest client, proporcionándole
    // un RestTemplate debidamente configurado que queda enlazado al mock server usando Mockito.
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

    BeerDTOPageImpl getPage() {
        return new BeerDTOPageImpl(Arrays.asList(getBeerDto()), 1, 25, 1);
    }
}
