package com.jmunoz.primarybean.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.jmunoz.primarybean.services.GreetingService;

@Controller
public class SetterInjectedController {
  
  private GreetingService greetingService;
  
  // La anotación @Qualifier se puede indicar aquí o a la izquierda del argumento a inyectar.
  // @Qualifier("setterGreetingBean")
  @Autowired
  public void setGreetingService(@Qualifier("setterGreetingBean") GreetingService greetingService) {
    this.greetingService = greetingService;
  }

  public String sayHello() {
    return greetingService.sayGreeting();
  }
}
