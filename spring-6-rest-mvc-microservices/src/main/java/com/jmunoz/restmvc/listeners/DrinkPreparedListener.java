package com.jmunoz.restmvc.listeners;

import com.jmunoz.restmvc.config.KafkaConfig;
import com.jmunoz.restmvc.repositories.BeerOrderLineRepository;
import guru.springframework.spring6restmvcapi.events.DrinkPreparedEvent;
import guru.springframework.spring6restmvcapi.model.BeerOrderLineStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DrinkPreparedListener {

    private final BeerOrderLineRepository beerOrderLineRepository;

    @KafkaListener(groupId = "DrinkPreparedListener", topics = KafkaConfig.DRINK_PREPARED_TOPIC)
    public void listen(DrinkPreparedEvent event) {
            beerOrderLineRepository.findById(event.getBeerOrderLine().getId()).ifPresentOrElse(beerOrderLineEntity -> {

           beerOrderLineEntity.setStatus(BeerOrderLineStatus.COMPLETE);

           beerOrderLineRepository.save(beerOrderLineEntity);
        }, () -> log.error("Beer Order Line Not Found!"));
    }
}
