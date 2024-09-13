package com.jmunoz.spring6datar2dbc.controllers;

import com.jmunoz.spring6datar2dbc.model.BeerDTO;
import com.jmunoz.spring6datar2dbc.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// Notar que se usa la misma anotación para Spring MVC que para Spring WebFlux MVC
@RestController
@RequiredArgsConstructor
public class BeerController {

    public static final String BEER_PATH = "/api/v2/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

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
}
