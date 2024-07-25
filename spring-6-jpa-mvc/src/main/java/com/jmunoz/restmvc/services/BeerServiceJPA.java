package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.mappers.BeerMapper;
import com.jmunoz.restmvc.model.BeerDto;
import com.jmunoz.restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Como ahora vamos a tener dos implementaciones de BeerService, este que usa JPA lo hacemos @Primary
@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    // Usamos el repositorio en conjunción con el mapper.
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDto> listBeers() {
        return List.of();
    }

    @Override
    public Optional<BeerDto> getBeerById(UUID id) {
        return Optional.empty();
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beer) {
        return null;
    }

    @Override
    public void updateBeerById(UUID beerId, BeerDto beer) {

    }

    @Override
    public void deleteBeerById(UUID beerId) {

    }

    @Override
    public void patchBeerById(UUID beerId, BeerDto beer) {

    }
}
