package com.jmunoz.spring6webclient.client;

import com.fasterxml.jackson.databind.JsonNode;
import reactor.core.publisher.Flux;

import java.util.Map;

public interface BeerClient {

    Flux<String> listBeer();

    Flux<Map> listBeerMap();

    Flux<JsonNode> listBeerJsonNode();
}
