package com.jmunoz.restmvc.listeners;

import com.jmunoz.restmvc.config.KafkaConfig;
import guru.springframework.spring6restmvcapi.events.DrinkRequestEvent;
import guru.springframework.spring6restmvcapi.events.OrderPlacedEvent;
import guru.springframework.spring6restmvcapi.model.BeerOrderLineDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DrinkSplitterRouter {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaListener(groupId = "DrinkSplitterRouter", topics = KafkaConfig.ORDER_PLACED_TOPIC)
    public void receive(@Payload OrderPlacedEvent orderPlacedEvent) {

        if (orderPlacedEvent.getBeerOrderDto() == null ||
                orderPlacedEvent.getBeerOrderDto().getBeerOrderLines() == null ||
                orderPlacedEvent.getBeerOrderDto().getBeerOrderLines().isEmpty()) {
            log.error("Invalid Order Placed Event");
            return;
        }

        orderPlacedEvent.getBeerOrderDto().getBeerOrderLines().forEach(beerOrderLine -> {
            switch (beerOrderLine.getBeer().getBeerStyle()) {
                case LAGER:
                    log.debug("Splitting LAGER Order");
                    sendIceColdBeer(beerOrderLine);
                    break;
                case STOUT:
                    log.debug("Splitting STOUT Order");
                    sendCoolBeer(beerOrderLine);
                    break;
                case GOSE:
                    log.debug("Splitting Gose Order");
                    sendColdBeer(beerOrderLine);
                    break;
                case PORTER:
                    log.debug("Splitting PORTER Order");
                    sendCoolBeer(beerOrderLine);
                    break;
                case ALE:
                    log.debug("Splitting ALE Order");
                    sendCoolBeer(beerOrderLine);
                    break;
                case WHEAT:
                    log.debug("Splitting WHEAT Order");
                    sendColdBeer(beerOrderLine);
                    break;
                case IPA:
                    log.debug("Splitting IPA Order");
                    sendCoolBeer(beerOrderLine);
                    break;
                case PALE_ALE:
                    log.debug("Splitting PALE_ALE Order");
                    sendCoolBeer(beerOrderLine);
                    break;
                case SAISON:
                    log.debug("Splitting SAISON Order");
                    sendIceColdBeer(beerOrderLine);
                    break;
            }
        });
    }

    private void sendIceColdBeer(BeerOrderLineDto beerOrderLineDto) {
        // send ice cold beer
        kafkaTemplate.send(KafkaConfig.DRINK_REQUEST_ICE_COLD_TOPIC, DrinkRequestEvent.builder()
                .beerOrderLineDto(beerOrderLineDto)
                .build());
    }

    private void sendColdBeer(BeerOrderLineDto beerOrderLineDto) {
        // send cold beer
        kafkaTemplate.send(KafkaConfig.DRINK_REQUEST_COLD_TOPIC, DrinkRequestEvent.builder()
                .beerOrderLineDto(beerOrderLineDto)
                .build());
    }

    private void sendCoolBeer(BeerOrderLineDto beerOrderLineDto) {
        // send cool beer
        kafkaTemplate.send(KafkaConfig.DRINK_REQUEST_COOL_TOPIC, DrinkRequestEvent.builder()
                .beerOrderLineDto(beerOrderLineDto)
                .build());
    }
}
