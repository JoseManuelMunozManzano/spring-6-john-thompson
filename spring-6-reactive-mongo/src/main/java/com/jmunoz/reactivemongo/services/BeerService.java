package com.jmunoz.reactivemongo.services;

import com.jmunoz.reactivemongo.model.BeerDTO;
import reactor.core.publisher.Mono;

public interface BeerService {

    Mono<BeerDTO> saveBeer(BeerDTO beerDTO);

    Mono<BeerDTO> getById(String beerId);
}
