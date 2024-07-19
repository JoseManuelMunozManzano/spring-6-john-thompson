package com.jmunoz.restmvc.controller;

import com.jmunoz.restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

// Usando Lombok para generar el constructor usando todos los argumentos en runtime.
// Argumentos declarados como private final.
// Para ver la clase generada, de nuevo ir a Maven y ejecutar el lifecycle compile.
// La clase generada se encuentra en la carpeta
// target/classes/com/jmunoz/restmvc/controller/BeerController
@AllArgsConstructor
@Controller
public class BeerController {

    private final BeerService beerService;
}
