package com.jmunoz.di.controllers;

import com.jmunoz.di.services.GreetingService;
import org.springframework.stereotype.Controller;

// Añadiendo este stereotype, indicamos a Spring que este controller
// es un componente de Spring.
@Controller
public class ConstructorInjectedController {

    // Buena práctica cuando es posible, es usar final.
    private final GreetingService greetingService;

    // Forma preferida de inyección de dependencias.
    // Automáticamente lo inyecta, no hace falta indicar el @Autowired
    // cuando solo hay un constructor.
    public ConstructorInjectedController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    public String sayHello() {
        return greetingService.sayGreeting();
    }
}
