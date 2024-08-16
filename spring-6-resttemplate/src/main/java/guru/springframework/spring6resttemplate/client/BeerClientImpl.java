package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    // RestTemplateBuilder está preconfigurado usando valores defaults que suelen venir bien.
    // Lo usamos para que pidan instancias de RestTemplates.
    private final RestTemplateBuilder restTemplateBuilder;

    // Más tarde en el curso, esta primera constante será una propiedad que Spring manejará en tiempo de configuración.
    private static final String BASE_URL = "http://localhost:8080";
    private static final String GET_BEER_PATH = "/api/v1/beer";

    @Override
    public Page<BeerDTO> listBeers() {
        // Lo primero siempre es obtener una instancia de RestTemplate, usando el builder.
        RestTemplate restTemplate = restTemplateBuilder.build();

        // Devolvemos String porque al final del día un JSON es un String.
        // Es decir, todavía no parseamos el JSON.
        //
        // No olvidar ejecutar el proyecto spring-6-db-relationships en el puerto 8080, en otra ventana de IntelliJ.
        //
        // ResponseEntity nos da acceso a to-do lo que hay en la respuesta, y suele usar genéricos
        // para indicar que va a ser el body, y eso afecta a como Spring Framework parseará la respuesta.
        //
        // Tal y como está no invocamos Jackson.
        ResponseEntity<String> stringResponse =
                restTemplate.getForEntity(BASE_URL + GET_BEER_PATH, String.class);


        // Podemos indicar que queremos un tipo de response diferente a la retornada, parseando el body.
        // Aquí Spring invoca Jackson para parsear el JSON a un mapa de Java.
        // Haciendo un debug vemos que el body>content se convierte a un Linked HashMap.
        //
        // Cuando no se sabe exactamente que devuelve una API, es útil usar un Map para obtener la información.
        // Nos da mucha flexibilidad en la respuesta, pero al final vamos a querer enlazarlo a BeerDto.
        ResponseEntity<Map> mapResponse =
                restTemplate.getForEntity(BASE_URL + GET_BEER_PATH, Map.class);

        System.out.println(stringResponse.getBody());

        return null;
    }
}
