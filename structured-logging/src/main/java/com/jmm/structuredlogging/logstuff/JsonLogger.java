package com.jmm.structuredlogging.logstuff;

import ch.qos.logback.classic.spi.ILoggingEvent;
import org.springframework.boot.json.JsonWriter;
import org.springframework.boot.logging.structured.StructuredLogFormatter;

public class JsonLogger implements StructuredLogFormatter<ILoggingEvent> {

    private final JsonWriter<ILoggingEvent> writer = JsonWriter.<ILoggingEvent>of((members) -> {
        members.add("Level", ILoggingEvent::getLevel);
        members.add("Message", ILoggingEvent::getFormattedMessage);
        members.add("timestamp", ILoggingEvent::getTimeStamp);
    }).withNewLineAtEnd();

    @Override
    public String format(ILoggingEvent event) {
        return this.writer.writeToString(event);
    }
}
