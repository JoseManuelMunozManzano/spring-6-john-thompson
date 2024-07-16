package com.jmunoz.di.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jmunoz.di.services.GreetingServiceImpl;

public class PropertyInjectedControllerTest {

  PropertyInjectedController propertyInjectedController;

  @BeforeEach
  void setUp() {
    propertyInjectedController = new PropertyInjectedController();

    // Si me olvido esta sentencia, greetingService es null.
    propertyInjectedController.greetingService = new GreetingServiceImpl();
  }

  @Test
  void sayHello() {
    System.out.println(propertyInjectedController.sayHello());
  }
}
