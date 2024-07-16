package com.jmunoz.di.controllers;

import org.springframework.stereotype.Controller;

import com.jmunoz.di.services.GreetingService;
import com.jmunoz.di.services.GreetingServiceImpl;

@Controller
public class MyController {
  
  private final GreetingService greetingService;

  public MyController() {
    // Estamos creando una dependencia sobre GreetingService, pero interna en el
    // controller. El controller tiene todo el control sobre como este service es
    // creado y manejado. Spring no interviene para nada y no hay inyección de
    // dependencias.
    // NO HACER ESTO!!!
    this.greetingService = new GreetingServiceImpl();
  }

  public String sayHello() {
    return greetingService.sayGreeting();
  }
}
