package com.jmunoz.restmvc.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Sin esta anotación, hablamos de la primera forma de manejar excepciones, es decir,
// cuando se usa @ExceptionHandler en el controlador de manera local.
//
// Con la anotación @ResponseStatus, hablamos de la tercera forma de manejar excepciones.
// Se lanza el status HTTP NOT_FOUND
// Con esto, no es necesario el @ControllerAdvice en la clase ExceptionController. No se va a usar.
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Value Not Found")
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
        super();
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }

    protected NotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
