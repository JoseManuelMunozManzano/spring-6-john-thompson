package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.model.BeerDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Se añade el uso de Optional de Java.
// La idea es que no va a ser el service el que lanza la excepción, sino el controller.
// Es decir, el service devuelve un Optional, y el controller hará un orElseThrow, para que,
// cuando es null, lance la excepción, pero la lanza el controller!!
public interface BeerService {

    List<BeerDto> listBeers();

    Optional<BeerDto> getBeerById(UUID id);

    BeerDto saveNewBeer(BeerDto beer);

    void updateBeerById(UUID beerId, BeerDto beer);

    void deleteBeerById(UUID beerId);

    void patchBeerById(UUID beerId, BeerDto beer);
}
