package com.jmmunoz.spring6coolservice.listeners;

import com.jmmunoz.spring6coolservice.config.KafkaConfig;
import guru.springframework.spring6restmvcapi.events.DrinkRequestEvent;
import guru.springframework.spring6restmvcapi.model.BeerDto;
import guru.springframework.spring6restmvcapi.model.BeerOrderLineDto;
import guru.springframework.spring6restmvcapi.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EmbeddedKafka(controlledShutdown = true, topics = {KafkaConfig.DRINK_REQUEST_COOL_TOPIC, KafkaConfig.DRINK_PREPARED_TOPIC}, partitions = 1)
class DrinkRequestListenerTest {

    @Autowired
    DrinkRequestListener drinkRequestListener;

    @Autowired
    DrinkPreparedListener drinkPreparedListener;

    @Test
    void listen() {
        drinkRequestListener.listenDrinkRequest(DrinkRequestEvent.builder()
                        .beerOrderLineDto(createDto())
                .build());

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            assertEquals(1, drinkPreparedListener.iceColdMessageCounter.get());
        });
    }

    public BeerOrderLineDto createDto() {
        return BeerOrderLineDto.builder()
                .beer(BeerDto.builder()
                        .id(UUID.randomUUID())
                        .beerStyle(BeerStyle.IPA)
                        .beerName("Test Beer")
                        .build())
                .build();
    }
}