package com.jmunoz.primarybean.controllers.i18n;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

import com.jmunoz.primarybean.services.GreetingService;

@Controller
public class Myi18NController {
  
  private final GreetingService greetingService;

  // Teniendo los dos services, EnglishGreetingService y SpanishGreetingService,
  // con el mismo nombre de bean, tenemos que usar algo para diferenciar cual
  // queremos inyectar. Usamos la anotación @Profile para indicar perfiles distintos.
  //
  // Desde el Test Myi18NController.java, usamos la anotación @ActiveProfiles
  // para indicar que profile está activo y queremos usar.
  public Myi18NController(@Qualifier("i18NService") GreetingService greetingService) {
    this.greetingService = greetingService;
  }

  public String sayHello() {
    return greetingService.sayGreeting();
  }
}
