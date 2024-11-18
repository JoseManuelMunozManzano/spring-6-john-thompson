package com.jmunoz.restmvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Segunda forma de manejar la excepción.
// Usando esta anotación, podemos manejar la excepción globalmente en todos los controllers.
// Se centralizan los gestores de excepciones y se puede personalizar la respuesta dada al cliente.
//
// Usando la tercera forma de manejar la excepción (@ResponseStatus, ver clase NotFoundException) ya no
// haría falta esta anotación ni @ExceptionHandler (se podría borrar esta clase perfectamente)
//
// Por tanto, para usar la segunda forma de manejar las excepciones, descomentar @ControllerAdvice y @ExceptionHandler,
// y comentar @ResponseStatus en la clase NotFoundException.
//
//@ControllerAdvice
public class ExceptionController {

//    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException() {

        // Aquí podría devolver un JSON body a los clientes con la información necesaria.
        return ResponseEntity.notFound().build();
    }
}
