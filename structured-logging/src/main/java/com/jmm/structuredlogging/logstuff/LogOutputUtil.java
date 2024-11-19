package com.jmm.structuredlogging.logstuff;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogOutputUtil implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.trace("Trace log");
        log.debug("Debugging log");
        // Por defecto el nivel de registro de Spring Boot es info, es decir
        // vamos a ver en la ejecución solo estos 3 últimos tipos de log.
        // Para evitar esto, cambiamos application.properties.
        log.info("Info log");
        log.warn("Hey, this is a warning!");
        log.error("Oops! We have an error");
    }
}
