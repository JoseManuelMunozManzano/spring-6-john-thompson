package com.jmunoz.restmvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Creo esta clase de Exception con ese tipo de excepción porque es el que me devuelve Spring al manejar
// la excepción de Data Validation.
// Devuelvo los campos que dan error, aunque esto devuelve demasiada información y no es amigable para su consumo.
// Lo que se hace es devolver solo la información que se necesita, es decir: el campo que falla y por qué falla.

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> handleBindErrors(MethodArgumentNotValidException exception) {

        // Usamos un Map para representar un JSON con la ayuda de Jackson.
        List<?> errorList = exception.getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
                    return errorMap;
                }).toList();

        // Demasiada información
        // return ResponseEntity.badRequest().body(exception.getBindingResult().getFieldErrors());
        //
        return ResponseEntity.badRequest().body(errorList);
    }
}
