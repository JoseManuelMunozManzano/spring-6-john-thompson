package com.jmunoz.spring6webclient.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.jmunoz.spring6webclient.model.BeerDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
public class BeerClientImpl implements BeerClient {

    public static final String BEER_PATH = "/api/v3/beer";
    private final WebClient webClient;

    // SpringBoot autoconfigura el servicio de WebClient.
    // La mejor práctica es usar el builder, es decir, no queremos directamente el WebClient, sino su builder.
    //
    // Inicializamos el web client a la url base localhost con puerto 8080.
    // Es decir, el back tiene que estar en el puerto 8080.
    //
    // Esto lo hemos cambiado para obtenerlo de la clase WebClientConfig.java que hemos creado.
    // Externalizándolo conseguimos un código más limpio.
    public BeerClientImpl(WebClient.Builder webClientBuilder) {
        //this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
        this.webClient = webClientBuilder.build();
    }

    // Al principio no usamos ningún mapeo con Jackson.
    // Solo queremos obtener la respuesta del endpoint.
    @Override
    public Flux<String> listBeer() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(String.class);
    }

    // Si no conocemos la estructura de la data, o es muy cambiante, podemos devolver un map Java.
    // Esta posibilidad es muy útil.
    @Override
    public Flux<Map> listBeerMap() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(Map.class);
    }

    // Si no conocemos la estructura de la data, otra técnica que podemos usar es devolver un nodo JSON Jackson.
    // Esta posibilidad es preferible sobre el map Java, porque nos permite usar una gran cantidad de métodos
    // muy útiles para navegar por el objeto JSON devuelto.
    @Override
    public Flux<JsonNode> listBeerJsonNode() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(JsonNode.class);
    }

    // Si conocemos la data, podemos usar WebClient para enlazarla a un DTO (un POJO).
    // Se ha creado BeerDTO y se va a enlazar, usando Jackson, el JSON devuelto (deserialización) a dicho DTO.
    // En este ejemplo no es necesario ningún mapeo porque el payload devuelto cuadra perfectamente
    // con nuestro DTO.
    @Override
    public Flux<BeerDTO> listBeerDtos() {
        return webClient.get().uri(BEER_PATH)
                .retrieve().bodyToFlux(BeerDTO.class);
    }

}
