package com.jmunoz.restmvc.controller;

import com.jmunoz.restmvc.model.Beer;
import com.jmunoz.restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {

    private final BeerService beerService;

    @PutMapping("{beerId}")
    public ResponseEntity<Beer> updatedById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
        beerService.updateBeerById(beerId, beer);

        // NO_CONTENT: Recibimos el Request y to-do ha ocurrido normalmente. No se devuelve nada.
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // @RequestMapping(method = RequestMethod.POST)
    @PostMapping
    public ResponseEntity<Beer> handlePost(@RequestBody Beer beer) {
        Beer savedBeer = beerService.saveNewBeer(beer);

        // Una buena práctica al trabajar con RESTful APIs es devolver al cliente
        // información sobre el objeto que se creó y la propiedad del header llamada location.

        // Esto va al header
        // La idea es tener un URL para acceder a ese id en concreto.
        // Es decir, podemos copiar, de los headers de la respuesta al hacer el POST, esa URL
        // e ir al endpoint Get Beer By Id y pegar ese URL.
        // Es como una ayuda a la hora de obtener el id.
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBeer.getId())
                .toUri();

        // Aquí devolvemos al cliente tanto la propiedad location como el registro creado
        return ResponseEntity.created(location).body(savedBeer);
    }

    @RequestMapping(method = RequestMethod.GET)
    private List<Beer> listBeers() {
        return beerService.listBeers();
    }

    @RequestMapping(value = "/{beerId}", method = RequestMethod.GET)
    public Beer getBeerById(@PathVariable("beerId") UUID beerId) {

        log.debug("Get Beer by Id - in controller");

        return beerService.getBeerById(beerId);
    }
}
