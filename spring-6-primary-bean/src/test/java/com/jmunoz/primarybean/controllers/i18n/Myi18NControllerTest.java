package com.jmunoz.primarybean.controllers.i18n;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ActiveProfiles;

// Activa el profile EN. El profile ES ni siquiera se carga en el contexto de Spring.
// Si no activamos uno de los dos profiles y no tenemos un profile por defecto,
// la ejecución de testSayHello() da error porque tenemos los dos services con el
// mismo nombre de bean.
//
// Si no activamos ningún profile, tenemos que tener un profile por defecto para que
// se active en estos casos. En este ejemplo el profile por defecto es SpanishGreetingService.java
//
// @ActiveProfiles("EN")
@SpringBootTest
public class Myi18NControllerTest {

  @Autowired
  Myi18NController myi18nController;

  // Depende de que profile esté activo, escribirá en consola Hello World o Hola Mundo.
  // Ver arriba @ActiveProfiles
  @Test
  void testSayHello() {
    System.out.println(myi18nController.sayHello());
  }
}
