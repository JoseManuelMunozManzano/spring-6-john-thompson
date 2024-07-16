package com.jmunoz.di.controllers;

import com.jmunoz.di.services.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

// Añadiendo este stereotype, indicamos a Spring que este controller
// es un componente de Spring.
@Controller
public class PropertyInjectedController {

  // Inyectando usando property con la anotación @Autowired
  // No recomendado.
  @Autowired
  GreetingService greetingService;

  public String sayHello() {
    return greetingService.sayGreeting();
  }
}
