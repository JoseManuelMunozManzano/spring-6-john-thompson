package com.jmunoz.di.services;

import org.springframework.stereotype.Service;

// Añadiendo este stereotype, indicamos a Spring que esta implementación del service
// es un componente de Spring.
@Service
public class GreetingServiceImpl implements GreetingService {

  @Override
  public String sayGreeting() {
    return "Hello Everyone From Base Service!!!";
  }
  
}
