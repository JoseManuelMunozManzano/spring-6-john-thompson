package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    // RestTemplateBuilder está preconfigurado usando valores defaults que suelen venir bien.
    // Lo usamos para que pidan instancias de RestTemplates.
    private final RestTemplateBuilder restTemplateBuilder;

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
        ResponseEntity<String> stringResponse =
                restTemplate.getForEntity("http://localhost:8080/api/v1/beer", String.class);
        System.out.println(stringResponse.getBody());

        return null;
    }
}
