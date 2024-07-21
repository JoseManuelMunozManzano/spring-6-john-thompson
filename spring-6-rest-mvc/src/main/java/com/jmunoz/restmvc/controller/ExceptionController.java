package com.jmunoz.restmvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Usando esta anotación, podemos manejar la excepción globalmente en todos los controllers.
// Se centralizan los gestores de excepciones y se puede personalizar la respuesta dada al cliente.
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException() {

        // Aquí podría devolver un JSON body a los clientes con la información necesaria.
        return ResponseEntity.notFound().build();
    }
}
