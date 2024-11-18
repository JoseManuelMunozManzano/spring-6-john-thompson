package com.jmunoz.restmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

// Habilitamos caché
@EnableCaching
@SpringBootApplication
public class RestmvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestmvcApplication.class, args);
	}

}
