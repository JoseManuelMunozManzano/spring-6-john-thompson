package com.jmunoz.di.controllers;

import com.jmunoz.di.services.GreetingService;

public class ConstructorInjectedController {

    // Buena práctica cuando es posible, es usar final.
    private final GreetingService greetingService;

    // Forma preferida de inyección de dependencias.
    // Ahora solo puedo instanciar la clase si paso greetingService,
    // lo que evita los errores por null.
    public ConstructorInjectedController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    public String sayHello() {
        return greetingService.sayGreeting();
    }
}
