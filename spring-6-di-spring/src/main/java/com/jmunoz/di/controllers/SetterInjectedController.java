package com.jmunoz.di.controllers;

import com.jmunoz.di.services.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

// Añadiendo este stereotype, indicamos a Spring que este controller
// es un componente de Spring.
@Controller
public class SetterInjectedController {

    private GreetingService greetingService;

    // Inyectando usando setter con la anotación @Autowired
    @Autowired
    public void setGreetingService(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    public String sayHello() {
        return greetingService.sayGreeting();
    }
}
