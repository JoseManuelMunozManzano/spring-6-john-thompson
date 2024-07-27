package com.jmunoz.restmvc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Creo esta clase de Exception con ese tipo de excepción porque es el que me devuelve Spring al manejar
// la excepción de Data Validation.
// Devuelvo los campos que dan error, aunque esto devuelve demasiada información.

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<?> handleBindErrors(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(exception.getBindingResult().getFieldErrors());
    }
}
