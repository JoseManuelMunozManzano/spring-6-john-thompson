package com.jmunoz.primarybean.services.i18n;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.jmunoz.primarybean.services.GreetingService;

// Los dos services EnglishGreetingService y SpanishGreetingService tienen el mismo nombre de bean.
@Profile("EN")
@Service("i18NService")
public class EnglishGreetingService implements GreetingService {

  @Override
  public String sayGreeting() {
    return "Hello World - EN";
  }
  
}
