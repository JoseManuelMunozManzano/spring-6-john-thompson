package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.model.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Se añade el uso de Optional de Java.
// La idea es que no va a ser el service el que lanza la excepción, sino el controller.
// Es decir, el service devuelve un Optional, y el controller hará un orElseThrow, para que,
// cuando es null, lance la excepción, pero la lanza el controller!!
public interface BeerService {

    List<Beer> listBeers();

    Optional<Beer> getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);

    void updateBeerById(UUID beerId, Beer beer);

    void deleteBeerById(UUID beerId);

    void patchBeerById(UUID beerId, Beer beer);
}
