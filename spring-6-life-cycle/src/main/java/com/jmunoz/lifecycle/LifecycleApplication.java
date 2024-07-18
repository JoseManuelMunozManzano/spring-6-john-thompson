package com.jmunoz.lifecycle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.jmunoz.lifecycle.controllers.MyController;

@SpringBootApplication
public class LifecycleApplication {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(LifecycleApplication.class, args);

		MyController controller = ctx.getBean(MyController.class);

		System.out.println(controller.sayHello());
	}

}
