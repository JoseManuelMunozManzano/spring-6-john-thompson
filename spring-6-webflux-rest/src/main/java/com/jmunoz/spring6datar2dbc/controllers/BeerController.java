package com.jmunoz.spring6datar2dbc.controllers;

import com.jmunoz.spring6datar2dbc.model.BeerDTO;
import com.jmunoz.spring6datar2dbc.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// Notar que se usa la misma anotación para Spring MVC que para Spring WebFlux MVC
@RestController
@RequiredArgsConstructor
public class BeerController {

    public static final String BEER_PATH = "/api/v2/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    @Value("${com.jmunoz.spring6.fromhttpurl}")
    private String httpUrl;

    private final BeerService beerService;

    // Devolvemos una lista de Beers, así que usamos Flux porque podemos obtener cero o muchos.
    // De nuevo, usamos la misma anotación que con Spring MVC, es decir, @GetMapping.
    @GetMapping(BEER_PATH)
    Flux<BeerDTO> listBeers() {

        return beerService.listBeers();
    }

    // Uso de Path Variable
    // Usamos Mono porque devuelve 0 o 1
    @GetMapping(BEER_PATH_ID)
    Mono<BeerDTO> getBeerById(@PathVariable("beerId") Integer beerId) {

        return beerService.getBeerById(beerId);
    }

    // En este caso devolvemos un Mono del tipo ResponseEntity de tipo Void, es decir, realmente
    // devuelve una respuesta vacía.
    @PostMapping(BEER_PATH)
    Mono<ResponseEntity<Void>> createNewBeer(@Validated @RequestBody BeerDTO beerDTO) {

        return beerService.saveNewBeer(beerDTO)
                .map(savedDto -> ResponseEntity.created(UriComponentsBuilder
                        // Usar savedDto, si no obtendremos null
                        .fromHttpUrl(httpUrl + BEER_PATH + "/" + savedDto.getId())
                        .build().toUri())
                        .build());
    }

    // Notar que esta vez no se devuelve un Mono<ResponseEntity<Void>>, sino directamente el ResponseEntity<Void>
    // En este caso hay que indicar subscribe() para que se haga el update correctamente.
    // Esto se ha hecho así para ver otra forma de hacer las cosas. Se podría haber hecho parecido a pathExistingBeer(),
    // usando Mono<ResponseEntity<Void>> sin usar el subscribe().
    @PutMapping(BEER_PATH_ID)
    ResponseEntity<Void> updateExistingBeer(@PathVariable("beerId") Integer beerId,
                                            @Validated @RequestBody BeerDTO beerDTO) {

        // Como se sabe el id, no necesitamos establecer el header location, como hicimos con Spring MVC.
        beerService.updateBeer(beerId, beerDTO).subscribe();

        return ResponseEntity.noContent().build();
    }

    @PatchMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> patchExistingBeer(@PathVariable("beerId") Integer beerId,
                                                 @Validated @RequestBody BeerDTO beerDTO) {

        return beerService.patchBeer(beerId, beerDTO)
                .map(patchedDto -> ResponseEntity.noContent().build());
    }

    @DeleteMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteById(@PathVariable("beerId") Integer beerId) {

        // Como deleteBeerById devuelve un Mono<Void>, esto realmente no es nada.
        // No llega a la función map() porque no hay nada, y el framework WebFlux lo trata como un status 200 (ok),
        // lo que causa que el testing falle al indicar que se espera un isNoContent()
        //
        //return beerService.deleteBeerById(beerId).map(response -> {
        //    return ResponseEntity.noContent().build();
        //});

        // Para que funcione correctamente, tanto el controller como nuestro test, hay que hacerlo así:
        return beerService.deleteBeerById(beerId)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
