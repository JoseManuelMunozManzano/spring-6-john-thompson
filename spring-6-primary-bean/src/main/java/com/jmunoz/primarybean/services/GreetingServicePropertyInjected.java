package com.jmunoz.primarybean.services;

import org.springframework.stereotype.Service;

// El nombre que se indica en @Service nos sirve como nombre concreto del Bean a la hora
// de indicarlo en la anotación @Qualifier
@Service("propertyGreetingService")
public class GreetingServicePropertyInjected implements GreetingService {

  @Override
  public String sayGreeting() {
    return "Friends don't let friends to property injection!!!";
  }
}
