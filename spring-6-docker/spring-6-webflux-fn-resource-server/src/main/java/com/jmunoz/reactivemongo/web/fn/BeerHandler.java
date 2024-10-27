package com.jmunoz.reactivemongo.web.fn;

import com.jmunoz.reactivemongo.model.BeerDTO;
import com.jmunoz.reactivemongo.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// El propósito de esta clase es manejar las peticiones que entran al framework.
// Es el handler, como un controller.
@Component
@RequiredArgsConstructor
public class BeerHandler {

    private final BeerService beerService;

    // Añadimos la validación.
    // Por defecto, SpringBoot va a autoconfigurarlo por nosotros.
    // No olvidar que en el pom debemos tener: spring-boot-starter-validation
    private final Validator validator;

    // Este méto-do lanzará una excepción si hay errores de validación.
    // En el contexto de Spring MVC, cuando se pasa un objeto (se puede indicar cualquier nombre),
    // se le llama binding.
    // Se indica que estamos haciendo un binding de propiedades bean.
    private void validate(BeerDTO beerDTO) {
        Errors errors = new BeanPropertyBindingResult(beerDTO, "beerDto");
        validator.validate(beerDTO, errors);

        // Esto indica a Spring que ha ocurrido un error de validación.
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }

        // Para utilizarlo necesitamos añadirlo a la cadena de eventos.
        // Ver que lo añadimos en los métodos create, update y patch
        // Se usa el méto-do doOnNext()
    }

    // Tratamos con ServerRequest y respondemos con ServerResponse.
    // En Spring Web MVC y Spring WebFlux dejamos al framework tratar con ellos, pero en
    // Spring WebFlux fn tratamos directamente con ellos.
    public Mono<ServerResponse> listBeers(ServerRequest request) {

        Flux<BeerDTO> flux;

        // Usando query parameters.
        // Puede ser listBeers() o findByBeerStyle(), en función de si tenemos ese queryParameter o no.
        if (request.queryParam("beerStyle").isPresent()) {
            flux = beerService.findByBeerStyle(request.queryParam("beerStyle").get());
        } else {
            flux = beerService.listBeers();
        }

        // El body espera un publisher, en este caso el Flux sobre BeerDTO que devuelve listBeers.
        // Para el tipo de conversión, Jackson se usa para renderizar a una respuesta JSON,
        // e indicamos BeerDTO.class para que el framework sepa como hacer el parse a esa respuesta JSON.
        return ServerResponse.ok()
                .body(flux, BeerDTO.class);
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
        //
        // Usando el méto-do doOnNext(), añadimos a la cadena de eventos las validaciones.
        // Ver que lo añadimos antes de llamar al méto-do save.
        return beerService.saveBeer(request.bodyToMono(BeerDTO.class)
                        .doOnNext(this::validate))
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
        //
        // Usando el méto-do doOnNext(), añadimos a la cadena de eventos las validaciones.
        return request.bodyToMono(BeerDTO.class)
                .doOnNext(this::validate)
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
        //
        // Usando el méto-do doOnNext(), añadimos a la cadena de eventos las validaciones.
        return request.bodyToMono(BeerDTO.class)
                .doOnNext(this::validate)
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
