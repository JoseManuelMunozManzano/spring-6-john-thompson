package com.jmunoz.spring6webclient.client;

import reactor.core.publisher.Flux;

public interface BeerClient {

    Flux<String> listBeer();
}
