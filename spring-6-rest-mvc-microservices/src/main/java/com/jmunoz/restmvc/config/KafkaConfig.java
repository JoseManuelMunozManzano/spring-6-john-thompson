package com.jmunoz.restmvc.config;

public class KafkaConfig {

    // Buena práctica de definir en un sitio (también podría haber sido application.properties) el topic
    public static final String ORDER_PLACED_TOPIC = "order.placed";
}
