package com.jmunoz.di.controllers;

import org.springframework.stereotype.Controller;

// El stereotype @Controller hará de esta clase un Bean de Spring.
@Controller
public class MyController {
  
  public String sayHello() {
    System.out.println("I'm in the controller");

    return "Hello Everyone!!!";
  }
}
