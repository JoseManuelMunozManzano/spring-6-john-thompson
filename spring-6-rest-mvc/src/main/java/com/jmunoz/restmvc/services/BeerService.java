package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {

    List<Beer> listBeers();

    Beer getBeerById(UUID id);
}
