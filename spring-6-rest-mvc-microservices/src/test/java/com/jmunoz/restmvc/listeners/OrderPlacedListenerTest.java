package com.jmunoz.restmvc.listeners;

import com.jmunoz.restmvc.config.KafkaConfig;
import guru.springframework.spring6restmvcapi.events.OrderPlacedEvent;
import guru.springframework.spring6restmvcapi.model.BeerDto;
import guru.springframework.spring6restmvcapi.model.BeerOrderDto;
import guru.springframework.spring6restmvcapi.model.BeerOrderLineDto;
import guru.springframework.spring6restmvcapi.model.BeerStyle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;

// @DirtiesContext reseteará el contexto de Spring Boot después de la ejecución.
@SpringBootTest
@EmbeddedKafka(controlledShutdown = true, topics = {KafkaConfig.ORDER_PLACED_TOPIC}, partitions = 1, kraft = true)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class OrderPlacedListenerTest {

    @Autowired
    KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    DrinkSplitterRouter drinkSplitter;

    @Autowired
    DrinkListenerKafkaConsumer drinkListenerKafkaConsumer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    OrderPlacedListener orderPlacedListener;

    @Autowired
    OrderPlacedKafkaListener orderPlacedKafkaListener;

    @BeforeEach
    void setUp() {
        kafkaListenerEndpointRegistry.getListenerContainers().forEach(container -> {
            ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
        });
    }

    @Test
    void listen() {
        OrderPlacedEvent orderPlacedEvent = OrderPlacedEvent.builder().beerOrderDto(BeerOrderDto.builder()
                        .id(UUID.randomUUID())
                .build()).build();

        orderPlacedListener.listen(orderPlacedEvent);

        await().atMost(5, TimeUnit.SECONDS).pollDelay(100, TimeUnit.MILLISECONDS)
                .until(orderPlacedKafkaListener.messageCounter::get, equalTo(1));
    }

    @Test
    void listenSplitter()  {
        drinkSplitter.receive(OrderPlacedEvent.builder()
                .beerOrderDto(buildOrder())
                .build());

        await().atMost(15, TimeUnit.SECONDS).pollDelay(100, TimeUnit.MILLISECONDS)
                .until(drinkListenerKafkaConsumer.iceColdMessageCounter::get, greaterThan(0));

        await().atMost(15, TimeUnit.SECONDS).pollDelay(100, TimeUnit.MILLISECONDS)
                .until(drinkListenerKafkaConsumer.coldMessageCounter::get, greaterThan(0));

        await().atMost(15, TimeUnit.SECONDS).pollDelay(100, TimeUnit.MILLISECONDS)
                .until(drinkListenerKafkaConsumer.coolMessageCounter::get, greaterThan(0));
    }

    BeerOrderDto buildOrder() {

        Set<BeerOrderLineDto> beerOrderLines = new HashSet<>();

        beerOrderLines.add(BeerOrderLineDto.builder()
                .beer(BeerDto.builder()
                        .id(UUID.randomUUID())
                        .beerStyle(BeerStyle.IPA)
                        .beerName("Test Beer")
                        .build())
                .build());

        //add lager
        beerOrderLines.add(BeerOrderLineDto.builder()
                .beer(BeerDto.builder()
                        .id(UUID.randomUUID())
                        .beerStyle(BeerStyle.LAGER)
                        .beerName("Test Beer")
                        .build())
                .build());

        //add gose
        beerOrderLines.add(BeerOrderLineDto.builder()
                .beer(BeerDto.builder()
                        .id(UUID.randomUUID())
                        .beerStyle(BeerStyle.GOSE)
                        .beerName("Test Beer")
                        .build())
                .build());

        return BeerOrderDto.builder()
                .id(UUID.randomUUID())
                .beerOrderLines(beerOrderLines)
                .build();
    }
}