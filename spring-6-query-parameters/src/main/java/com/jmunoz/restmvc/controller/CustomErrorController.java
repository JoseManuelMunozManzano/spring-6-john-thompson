package com.jmunoz.restmvc.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
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

    // Manejamos errores de violaciones de constraints de la capa BBDD que burbujean hacia el controller.
    // Con esta excepción se ha hecho el Rollback automático.
    @ExceptionHandler
    ResponseEntity<?> handleJPAViolations(TransactionSystemException exception) {
        ResponseEntity.BodyBuilder responseEntity = ResponseEntity.badRequest();

        // Como se ve en el test BeerControllerIT, método testPatchBeerBadName(), las excepciones son:
        // Genérica - TransactionSystemException
        // La que nos interesa - ConstraintViolationException
        // Y la última que se lanza con el rollback - RollbackException
        if (exception.getCause().getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException ve = (ConstraintViolationException) exception.getCause().getCause();

            List<Map<String, String>> errors = ve.getConstraintViolations().stream()
                    .map(constraintViolation -> {
                        Map<String, String> errMap = new HashMap<>();
                        errMap.put(constraintViolation.getPropertyPath().toString(),
                                constraintViolation.getMessage());
                        return errMap;
                    }).toList();

            return responseEntity.body(errors);
        }

        return responseEntity.build();
    }
}
