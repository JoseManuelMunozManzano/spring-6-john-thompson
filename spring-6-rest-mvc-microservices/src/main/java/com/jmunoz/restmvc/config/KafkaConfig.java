package com.jmunoz.restmvc.config;

public class KafkaConfig {

    // Buena práctica de definir en un sitio (también podría haber sido application.properties) el topic
    public static final String ORDER_PLACED_TOPIC = "order.placed";
    public static final String DRINK_REQUEST_ICE_COLD_TOPIC = "drink.request.icecold";
    public static final String DRINK_REQUEST_COLD_TOPIC = "drink.request.cold";
    public static final String DRINK_REQUEST_COOL_TOPIC = "drink.request.cool";
    public static final String DRINK_PREPARED_TOPIC = "drink.prepared";
}
