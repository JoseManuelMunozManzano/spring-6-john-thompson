package com.jmunoz.spring6webclient.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class BeerClientImpl implements BeerClient {

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
        return webClient.get().uri("/api/v3/beer", String.class)
                .retrieve().bodyToFlux(String.class);
    }
}
