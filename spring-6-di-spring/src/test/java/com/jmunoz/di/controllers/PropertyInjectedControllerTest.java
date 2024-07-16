package com.jmunoz.di.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.jmunoz.di.services.GreetingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// Cargamos el contexto de Spring.
// Cuando se ejecute el contexto, buscará en el contexto de la aplicación, haciendo
// el escaneo de componentes.
@SpringBootTest
public class PropertyInjectedControllerTest {

  @Autowired
  PropertyInjectedController propertyInjectedController;

  // Usando la inyección de dependencias de Spring, esto ya no hace falta.
  // Solo tenemos que indicar @SpringBootTest en la declaración de la clase
  // y @Autowired en la declaración del controller y con eso inyectamos el
  // controller, donde a su vez se está inyectando el service.
  //
//  @BeforeEach
//  void setUp() {
//    propertyInjectedController = new PropertyInjectedController();
//
//    propertyInjectedController.greetingService = new GreetingServiceImpl();
//  }

  @Test
  void sayHello() {
    System.out.println(propertyInjectedController.sayHello());
  }
}
