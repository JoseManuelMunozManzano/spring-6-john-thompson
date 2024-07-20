package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.model.Beer;
import com.jmunoz.restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

// Usando Lombok para tener logging.
@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    @Override
    public Beer getBeerById(UUID id) {

        // Usando logger.
        // Solo por indicar la anotación tenemos disponible la propiedad log.
        log.debug("Get Beer by Id - in service. Id: " + id.toString());

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
