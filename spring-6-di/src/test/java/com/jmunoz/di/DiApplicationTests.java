package com.jmunoz.di;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.jmunoz.di.controllers.MyController;

// Esta anotación indica al test que cree el contexto de Spring.
@SpringBootTest
class DiApplicationTests {

	// Spring context inyecta el contexto gracias a @Autowired
	@Autowired
	ApplicationContext applicationContext;

	// Segunda forma de obtener la instancia del controlador, usando inyección del controlador
	// con @Autowired.
	@Autowired
	MyController myController;

	@Test
	void testAutowireOfController() {
		// Utilización del controller con la segunda forma de instanciarlo.
		System.out.println(myController.sayHello());
	}

	@Test
	void testGetControllerFromCtx() {
		// Primera forma de obtener la instancia del controlador usando el contexto de Spring, y utilización.
		MyController myController = applicationContext.getBean(MyController.class);

		System.out.println(myController.sayHello());
	}

	@Test
	void contextLoads() {
	}

}
