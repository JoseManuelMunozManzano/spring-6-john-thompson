package com.jmunoz.restmvc.controller;

import com.jmunoz.restmvc.model.Beer;
import com.jmunoz.restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import java.util.UUID;

// Usando Project Lombok
// Para ver la clase generada, de nuevo ir a Maven y ejecutar el lifecycle compile.
// La clase generada se encuentra en la carpeta
// target/classes/com/jmunoz/restmvc/controller/BeerController
//
// @AllArgsConstructor
// Genera el constructor usando todos los argumentos en runtime.
// Argumentos declarados como private final.
//
// @Slf4j
// Usando Lombok para tener logging.
@Slf4j
@AllArgsConstructor
@Controller
public class BeerController {

    private final BeerService beerService;

    public Beer getBeerById(UUID id) {

        // Usando logger.
        // Solo por indicar la anotación tenemos disponible la propiedad log.
        log.debug("Get Beer by Id - in controller");

        return beerService.getBeerById(id);
    }
}
