package com.jmunoz.reactivemongo.web.fn;

import com.jmunoz.reactivemongo.model.BeerDTO;
import com.jmunoz.reactivemongo.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
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
        // Si hay algún error entra por la parte de switchIfEmpty() y devuelve un 404.
        // Hay que especificar el tipo de retorno, que es BeerDTO.class.
        return ServerResponse
                .ok()
                .body(beerService.getById(request.pathVariable("beerId"))
                                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND))),
                        BeerDTO.class);
    }

    public Mono<ServerResponse> createNewBeer(ServerRequest request) {

        // Del request obtenemos el body y lo pasamos a Mono, usando BeerDTO como tipo destino.
        // Obtenemos un publisher (beerDTO) y lo usamos para construir un ServerResponse, devolviendo
        // una URL, el location, en el header.
        return beerService.saveBeer(request.bodyToMono(BeerDTO.class))
                .flatMap(beerDTO -> ServerResponse
                        .created(UriComponentsBuilder
                                .fromPath(BeerRouterConfig.BEER_PATH_ID)
                                .build(beerDTO.getId()))
                        .build());
    }

    public Mono<ServerResponse> updateBeerById(ServerRequest request) {

        // Del body obtenemos el tipo BeerDTO.class.
        // Obtenemos un publisher beerDTO y lo actualizamos.
        // Obtenemos otro publisher savedDto y devolvemos el ServerResponse de noContent()
        //
        // Si hay algún error entra por la parte de switchIfEmpty() y devuelve un 404.
        return request.bodyToMono(BeerDTO.class)
                .flatMap(beerDTO -> beerService
                        .updateBeer(request.pathVariable("beerId"), beerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDto -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> patchBeerById(ServerRequest request) {

        // Del body obtenemos el tipo BeerDTO.class.
        // Obtenemos un publisher beerDTO y lo actualizamos.
        // Obtenemos otro publisher savedDto y devolvemos el ServerResponse de noContent()
        //
        // Si hay algún error entra por la parte de switchIfEmpty() y devuelve un 404.
        return request.bodyToMono(BeerDTO.class)
                .flatMap(beerDTO -> beerService
                        .patchBeer(request.pathVariable("beerId"), beerDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDto -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> deleteBeerById(ServerRequest request) {

        // Buscamos el id del beer a borrar. Si no lo encontramos devuelve un 404.
        //
        // Si lo encontramos obtenemos un publisher beerDTO.
        // Cuando el resultado es Mono<Void>, usamos el méto-do then() que va a devolver
        // la señal desde el model. Lo importante es que usando then(), si hay una señal
        // de error en el delete, se transmite el error.
        return beerService.getById(request.pathVariable("beerId"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(beerDTO -> beerService.deleteBeerById(beerDTO.getId()))
                .then(ServerResponse.noContent().build());
    }
}
