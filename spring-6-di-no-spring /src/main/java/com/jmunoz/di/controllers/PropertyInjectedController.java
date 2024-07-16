package com.jmunoz.di.controllers;

import com.jmunoz.di.services.GreetingService;

public class PropertyInjectedController {

  // Peor forma de inyección de dependencias.
  // Aquí el problema es que se crea el objeto antes de que ocurra
  // la inyección de dependencias, por lo que greetingService puede ser null.
  // Ver testing.
  GreetingService greetingService;

  public String sayHello() {
    return greetingService.sayGreeting();
  }
}
