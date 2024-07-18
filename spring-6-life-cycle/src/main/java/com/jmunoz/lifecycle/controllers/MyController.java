package com.jmunoz.lifecycle.controllers;

import org.springframework.stereotype.Controller;

import com.jmunoz.lifecycle.services.GreetingService;

@Controller
public class MyController {

  private final GreetingService greetingService;

  public MyController(GreetingService greetingService) {
    this.greetingService = greetingService;

  }
  
  public void beforeInit() {
    System.out.println("## - Before Init - Called by Bean Post Processor");
  }
  
  public void afterInit() {
    System.out.println("## - After Init - Called by Bean Post Processor");
  }  
  
  public String sayHello() {
    System.out.println("I'm in the controller");
    
    return greetingService.sayGreeting();
  }
}
