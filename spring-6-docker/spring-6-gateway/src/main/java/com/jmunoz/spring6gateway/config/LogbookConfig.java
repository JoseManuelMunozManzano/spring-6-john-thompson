package com.jmunoz.spring6gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.HttpLogFormatter;
import org.zalando.logbook.Sink;
import org.zalando.logbook.json.JsonHttpLogFormatter;
import org.zalando.logbook.logstash.LogstashLogbackSink;

// Con esta configuración obtenemos un formateador de logs JSON y se aplica a Logstash.
// Sin esta configuración (ni su dependencia logbook-logstash), en la consola de ejecución
// obtenemos un JSON payload tipo String escapado, cosa que no queremos.
@Configuration
public class LogbookConfig {

    @Bean
    public Sink LogbookLogStash() {
        HttpLogFormatter formatter = new JsonHttpLogFormatter();
        LogstashLogbackSink sink = new LogstashLogbackSink(formatter);
        return sink;
    }
}
