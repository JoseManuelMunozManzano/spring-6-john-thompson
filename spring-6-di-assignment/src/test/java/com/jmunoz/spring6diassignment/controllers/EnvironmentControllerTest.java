package com.jmunoz.spring6diassignment.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// Jugar con los distintos profiles (dev, qa, uat, prod) o comentar la línea para
// incluir en el contexto de Spring el default profile (en este caso el dev)
//
// Se pueden indicar distintos profiles (de distintos tipos de services)
// Ejemplo: @ActiveProfiles({"prod", "EN"}) donde el entorno es Producción en Inglés.
@ActiveProfiles("prod")
@SpringBootTest
public class EnvironmentControllerTest {

  @Autowired
  EnvironmentController controller;

  @Test
  void testGetEnvironment() {
    System.out.println(controller.getEnvironment());
  }
}
