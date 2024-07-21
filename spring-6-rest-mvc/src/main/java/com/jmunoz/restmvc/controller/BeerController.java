package com.jmunoz.restmvc.controller;

import com.jmunoz.restmvc.model.Beer;
import com.jmunoz.restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerController {

    // Refactoring
    // No lo he hecho en Customer para ver la diferencia.
    public static final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    private final BeerService beerService;

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity<Beer> updateBeerPatchById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
        beerService.patchBeerById(beerId, beer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Funciona tanto /{customerId} como {customerId} sin la /
    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity<Beer> deleteById(@PathVariable("beerId") UUID beerId) {
        beerService.deleteBeerById(beerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity<Beer> updatedById(@PathVariable("beerId") UUID beerId, @RequestBody Beer beer) {
        beerService.updateBeerById(beerId, beer);

        // NO_CONTENT: Recibimos el Request y to-do ha ocurrido normalmente. No se devuelve nada.
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // @RequestMapping(method = RequestMethod.POST)
    @PostMapping(BEER_PATH)
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

    @GetMapping(value = BEER_PATH)
    private List<Beer> listBeers() {
        return beerService.listBeers();
    }

    @GetMapping(value = BEER_PATH_ID)
    public Beer getBeerById(@PathVariable("beerId") UUID beerId) {

        log.debug("Get Beer by Id - in controller");

        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    // Manejador de excepciones.
    // Vamos a devolver una nueva ResponseEntity.
    // Para que la excepción la maneje el framework, lo anotamos con @ExceptionHandler
    // e indicamos las clases de excepciones que queremos que se manejen.
    // Si, en este controlador, alguno de los métodos lanza una excepción del tipo NotFoundException,
    // se maneja en este método handler.
    // Indicar que en este método tenemos control total sobre lo que queremos devolver al cliente.
    //
    // La desventaja de este enfoque es que solo va a manejar la excepción de los métodos de este controller.
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Beer> handleNotFoundException() {
        log.debug("In exception handler");

        return ResponseEntity.notFound().build();
    }

}
