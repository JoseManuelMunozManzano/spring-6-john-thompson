package com.jmmunoz.spring6coldservice.listeners;

import com.jmmunoz.spring6coldservice.config.KafkaConfig;
import com.jmmunoz.spring6coldservice.services.DrinkRequestProcessor;
import guru.springframework.spring6restmvcapi.events.DrinkPreparedEvent;
import guru.springframework.spring6restmvcapi.events.DrinkRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DrinkRequestListener {

    private final DrinkRequestProcessor drinkRequestProcessor;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // Recibimos el evento
    @KafkaListener(groupId = "ColdListener", topics = KafkaConfig.DRINK_REQUEST_COLD_TOPIC)
    public void listenDrinkRequest(DrinkRequestEvent event) {
        log.debug("I am listening - drink request");

        // Procesamos
        drinkRequestProcessor.processDrinkRequest(event);

        // Enviamos el evento de bebida preparada
        kafkaTemplate.send(KafkaConfig.DRINK_PREPARED_TOPIC, DrinkPreparedEvent.builder()
                .beerOrderLine(event.getBeerOrderLineDto())
                .build());

    }
}