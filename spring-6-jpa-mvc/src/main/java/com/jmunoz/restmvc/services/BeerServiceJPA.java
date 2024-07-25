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
import java.util.concurrent.atomic.AtomicReference;

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

        // Está bien, si no encuentra nada, que devuelva una lista vacía.
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::beerEntityToBeerDto)
                .toList();
    }

    @Override
    public Optional<BeerDto> getBeerById(UUID id) {
        // Para una búsqueda concreta, si debemos indicar si no se ha encontrado
        // el elemento.
        return Optional.ofNullable(beerMapper.beerEntityToBeerDto(beerRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beer) {
        return beerMapper.beerEntityToBeerDto(beerRepository.save(beerMapper.beerDtoToBeerEntity(beer)));
    }

    // Las funciones lambda no actualizan ninguna variable local (foundBeer), ya que son effective final o final.
    // Cuando queremos actualizar un valor dentro de una función lambda, se usa AtomicReference.
    @Override
    public Optional<BeerDto> updateBeerById(UUID beerId, BeerDto beer) {
        AtomicReference<Optional<BeerDto>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            foundBeer.setBeerName(beer.getBeerName());
            foundBeer.setBeerStyle(beer.getBeerStyle());
            foundBeer.setUpc(beer.getUpc());
            foundBeer.setPrice(beer.getPrice());
            beerRepository.save(foundBeer);

            atomicReference.set(Optional.of(beerMapper
                    .beerEntityToBeerDto(beerRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public void deleteBeerById(UUID beerId) {

        beerRepository.deleteById(beerId);
    }

    @Override
    public void patchBeerById(UUID beerId, BeerDto beer) {

    }
}
