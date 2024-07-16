package com.jmunoz.primarybean.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.jmunoz.primarybean.services.GreetingService;

@Controller
public class MyController {
  
  private final GreetingService greetingService;

  // Si no indicamos @Qualifier, por defecto Spring inyecta la implementación
  // anotada con @Primary.
  public MyController(@Qualifier("greetingServiceImpl") GreetingService greetingService) {
    this.greetingService = greetingService;
  }

  public String sayHello() {
    return greetingService.sayGreeting();
  }
}
