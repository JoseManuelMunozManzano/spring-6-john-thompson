package com.jmunoz.di.controllers;

import com.jmunoz.di.services.GreetingService;

public class SetterInjectedController {

    private GreetingService greetingService;

    // Segunda peor forma de inyección de dependencias.
    // Aquí el problema es que si se instancia la clase, pero no llamamos
    // al setter, greetingService es null.
    // Ver testing.
    public void setGreetingService(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    public String sayHello() {
        return greetingService.sayGreeting();
    }
}
