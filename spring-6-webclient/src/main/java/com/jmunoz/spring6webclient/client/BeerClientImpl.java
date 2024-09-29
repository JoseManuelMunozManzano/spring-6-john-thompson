package com.jmunoz.spring6webclient.client;

import com.fasterxml.jackson.databind.JsonNode;
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
    // Inicializamos el web client a la url base localhost con puerto 8080.
    // Es decir, el back tiene que estar en el puerto 8080.
    public BeerClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }

    // Al principio no usamos ningún mapeo con Jackson.
    // Solo queremos obtener la respuesta del endpoint.
    @Override
    public Flux<String> listBeer() {
        return webClient.get().uri(BEER_PATH, String.class)
                .retrieve().bodyToFlux(String.class);
    }

    // Si no conocemos la estructura de la data, o es muy cambiante, podemos devolver un map Java.
    // Esta posibilidad es muy útil.
    @Override
    public Flux<Map> listBeerMap() {
        return webClient.get().uri(BEER_PATH, Map.class)
                .retrieve().bodyToFlux(Map.class);
    }

    // Si no conocemos la estructura de la data, otra técnica que podemos usar es devolver un nodo JSON Jackson.
    // Esta posibilidad es preferible sobre el map Java, porque nos permite usar una gran cantidad de métodos
    // muy útiles para navegar por el objeto JSON devuelto.
    @Override
    public Flux<JsonNode> listBeerJsonNode() {
        return webClient.get().uri(BEER_PATH, JsonNode.class)
                .retrieve().bodyToFlux(JsonNode.class);
    }
}
