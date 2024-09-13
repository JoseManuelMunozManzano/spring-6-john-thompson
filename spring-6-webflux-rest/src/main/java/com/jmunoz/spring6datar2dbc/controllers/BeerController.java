package com.jmunoz.spring6datar2dbc.controllers;

import com.jmunoz.spring6datar2dbc.model.BeerDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

// Notar que se usa la misma anotación para Spring MVC que para Spring WebFlux MVC
@RestController
public class BeerController {

    public static final String BEER_PATH = "/api/v2/beer";

    // Devolvemos una lista de Beers, así que usamos Flux porque podemos obtener cero o muchos.
    // De nuevo, usamos la misma anotación que con Spring MVC, es decir, @GetMapping.
    @GetMapping(BEER_PATH)
    Flux<BeerDTO> listBeers() {

        return Flux.just(BeerDTO.builder().id(1).build(),
                BeerDTO.builder().id(2).build());
    }
}
