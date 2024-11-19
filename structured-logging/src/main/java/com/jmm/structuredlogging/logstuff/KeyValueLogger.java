package com.jmm.structuredlogging.logstuff;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.springframework.boot.logging.structured.StructuredLogFormatter;

public class KeyValueLogger implements StructuredLogFormatter<ILoggingEvent> {

    @Override
    public String format(ILoggingEvent event) {
        // Hay más propiedades, pero solo vemos estas.
        // Veremos nuestros logs como pares de clave-valor
        return "level= " + event.getLevel() + ", message= " + event.getFormattedMessage();
    }
}
