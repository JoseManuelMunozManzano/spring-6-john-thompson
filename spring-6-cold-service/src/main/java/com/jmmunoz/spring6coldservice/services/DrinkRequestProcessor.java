package com.jmmunoz.spring6coldservice.services;

import guru.springframework.spring6restmvcapi.events.DrinkRequestEvent;

public interface DrinkRequestProcessor {

    void processDrinkRequest(DrinkRequestEvent event);
}
