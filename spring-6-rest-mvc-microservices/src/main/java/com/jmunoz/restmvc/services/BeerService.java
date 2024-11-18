package com.jmunoz.restmvc.services;

import guru.springframework.spring6restmvcapi.model.BeerDto;
import guru.springframework.spring6restmvcapi.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

// Se añade el uso de Optional de Java.
// La idea es que no va a ser el service el que lanza la excepción, sino el controller.
// Es decir, el service devuelve un Optional, y el controller hará un orElseThrow, para que,
// cuando es null, lance la excepción, pero la lanza el controller!!
public interface BeerService {

    Page<BeerDto> listBeers(Integer pageNumber, Integer pageSize);

    Page<BeerDto> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle, Integer pageNumber, Integer pageSize);

    Page<BeerDto> listBeersByNameContainingIgnoreCase(String beerName, Integer pageNumber, Integer pageSize);

    Page<BeerDto> listBeersByStyle(BeerStyle beerStyle, Integer pageNumber, Integer pageSize);

    Optional<BeerDto> getBeerById(UUID id);

    BeerDto saveNewBeer(BeerDto beer);

    Optional<BeerDto> updateBeerById(UUID beerId, BeerDto beer);

    Boolean deleteBeerById(UUID beerId);

    Optional<BeerDto> patchBeerById(UUID beerId, BeerDto beer);
}
