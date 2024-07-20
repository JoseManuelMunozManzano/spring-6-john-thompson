package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.model.Beer;

import java.util.UUID;

public interface BeerService {

    Beer getBeerById(UUID id);
}
