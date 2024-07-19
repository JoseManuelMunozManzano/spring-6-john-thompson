package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.model.Beer;
import com.jmunoz.restmvc.model.BeerStyle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class BeerServiceImpl implements BeerService {

    @Override
    public Beer getBeerById(UUID id) {
        // Usando el patrón Builder que hemos construido con Lombok en la clase Beer.
        return Beer.builder()
                .id(id)
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }
}
