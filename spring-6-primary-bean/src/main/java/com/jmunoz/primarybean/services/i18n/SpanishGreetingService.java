package com.jmunoz.primarybean.services.i18n;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.jmunoz.primarybean.services.GreetingService;

// Los dos services EnglishGreetingService y SpanishGreetingService tienen el mismo nombre de bean.
// Indicamos un nombre de profile.
//
// Indicamos también, en este caso, que este profile es el de por defecto, es decir, si no
// indicamos un nombre de profile, coge el default, que en este caso es este.
// NOTA: Se pueden indicar más nombres de profile para indicar que este service va a estar
//       en múltiples profiles.
@Profile({"ES", "default"})
@Service("i18NService")
public class SpanishGreetingService implements GreetingService {

  @Override
  public String sayGreeting() {
    return "Hola Mundo - ES";
  }
  
}
