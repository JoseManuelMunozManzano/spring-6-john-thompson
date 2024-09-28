package com.jmunoz.reactivemongo.web.fn;

import com.jmunoz.reactivemongo.model.BeerDTO;
import com.jmunoz.reactivemongo.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

// El propósito de esta clase es manejar las peticiones que entran al framework.
// Es el handler, como un controller.
@Component
@RequiredArgsConstructor
public class BeerHandler {

    private final BeerService beerService;

    // Tratamos con ServerRequest y respondemos con ServerResponse.
    // En Spring Web MVC y Spring WebFlux dejamos al framework tratar con ellos, pero en
    // Spring WebFlux fn tratamos directamente con ellos.
    public Mono<ServerResponse> listBeers(ServerRequest request) {

        // El body espera un publisher, en este caso el Flux sobre BeerDTO que devuelve listBeers.
        // Para el tipo de conversión, Jackson se usa para renderizar a una respuesta JSON,
        // e indicamos BeerDTO.class para que el framework sepa como hacer el parse a esa respuesta JSON.
        return ServerResponse.ok()
                .body(beerService.listBeers(), BeerDTO.class);
    }

    public Mono<ServerResponse> getBeerById(ServerRequest request) {

        // El beerId lo obtenemos del pathVariable.
        // Hay que especificar el tipo de retorno, que es BeerDTO.class.
        return ServerResponse
                .ok()
                .body(beerService.getById(request.pathVariable("beerId")), BeerDTO.class);
    }
}
