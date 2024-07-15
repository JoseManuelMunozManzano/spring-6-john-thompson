package com.jmunoz.di;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.jmunoz.di.controllers.MyController;

@SpringBootApplication
public class DiApplication {

	public static void main(String[] args) {

		// Obtenemos el contexto de la aplicación.
		ApplicationContext ctx = SpringApplication.run(DiApplication.class, args);

		// Del contexto de Spring, proporciona la instancia del bean MyController.
		MyController controller = ctx.getBean(MyController.class);

		System.out.println("In Main Method");

		System.out.println(controller.sayHello());
	}

}
