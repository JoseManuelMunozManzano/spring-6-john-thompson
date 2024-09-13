package com.jmunoz.spring6datar2dbc.services;

import com.jmunoz.spring6datar2dbc.model.BeerDTO;
import reactor.core.publisher.Flux;

public interface BeerService {

    Flux<BeerDTO> listBeers();
}
