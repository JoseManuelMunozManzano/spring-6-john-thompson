package com.jmmunoz.spring6coolservice.services;

import guru.springframework.spring6restmvcapi.events.DrinkRequestEvent;

public interface DrinkRequestProcessor {

    void processDrinkRequest(DrinkRequestEvent event);
}
