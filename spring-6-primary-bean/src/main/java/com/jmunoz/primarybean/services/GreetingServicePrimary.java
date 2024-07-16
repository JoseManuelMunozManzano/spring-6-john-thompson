package com.jmunoz.primarybean.services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

// Con la anotación @Primary, indicamos a Spring que, independientemente de las implementaciones
// que haya de GreetingService en el contexto de Spring, Spring inyectará esta.
// Pero se puede sobreescribir este funcionamiento por defecto con la anotación @Qualifier (Ver controller)
@Primary
@Service
public class GreetingServicePrimary implements GreetingService {

  @Override
  public String sayGreeting() {
    return "Hello From the Primary Bean!!";
  }
}
