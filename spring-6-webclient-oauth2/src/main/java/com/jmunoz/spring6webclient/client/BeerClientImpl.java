package com.jmunoz.spring6webclient.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.jmunoz.spring6webclient.model.BeerDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class BeerClientImpl implements BeerClient {

    public static final String BEER_PATH = "/api/v3/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

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

    @Override
    public Mono<BeerDTO> getBeerById(String id) {
        // Se usa el uriBuilder
        // En el build() pasamos las variables que queremos enlazar.
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID)
                        .build(id))
                .retrieve().bodyToMono(BeerDTO.class);
    }

    // Usando query Parameters.
    @Override
    public Flux<BeerDTO> getBeerByBeerStyle(String beerStyle) {
        // Se usa el uriBuilder
        // Indicamos el nombre y su valor
        return webClient.get().uri(uriBuilder -> uriBuilder
                        .path(BEER_PATH)
                        .queryParam("beerStyle", beerStyle).build())
                .retrieve().bodyToFlux(BeerDTO.class);
    }

    // La forma en la que se ejecuta la API, solo devuelve en el header la propiedad Location.
    // Pero nosotros queremos hacer el post para crear el objeto y luego devolverlo.
    // Por lo tanto, devolvemos el id y volvemos a llamar a la API para obtener el objeto salvado.
    @Override
    public Mono<BeerDTO> createBeer(BeerDTO beerDTO) {
        return webClient.post()
                .uri(BEER_PATH)
                // Pasamos el body
                .body(Mono.just(beerDTO), BeerDTO.class)
                // Post devuelve una respuesta vacía.
                .retrieve().toBodilessEntity()
                // La respuesta vacía la transformamos en obtener el id de la propiedad Location del header.
                .flatMap(voidResponseEntity -> Mono.just(voidResponseEntity
                        .getHeaders().get("Location").get(0)))
                .map(path -> path.split("/")[path.split("/").length - 1])
                .flatMap(this::getBeerById);
    }

    @Override
    public Mono<BeerDTO> updateBeer(BeerDTO beerDto) {
        return webClient.put()
                .uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID).build(beerDto.getId()))
                .body(Mono.just(beerDto), BeerDTO.class)
                .retrieve()
                .toBodilessEntity()
                .flatMap(voidResponseEntity -> getBeerById(beerDto.getId()));
    }

    @Override
    public Mono<BeerDTO> patchBeer(BeerDTO beerDto) {
        return webClient.patch()
                .uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID).build(beerDto.getId()))
                .body(Mono.just(beerDto), BeerDTO.class)
                .retrieve()
                .toBodilessEntity()
                .flatMap(voidResponseEntity -> getBeerById(beerDto.getId()));
    }

    @Override
    public Mono<Void> deleteBeer(String id) {
        return webClient.delete()
                .uri(uriBuilder -> uriBuilder.path(BEER_PATH_ID).build(id))
                .retrieve()
                .toBodilessEntity()
                // Para devolver un Mono<Void>
                .then();
    }


}
