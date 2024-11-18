package com.jmunoz.restmvc.services;

import guru.springframework.spring6restmvcapi.model.BeerDto;
import guru.springframework.spring6restmvcapi.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private Map<UUID, BeerDto> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        BeerDto beer1 = BeerDto.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDto beer2 = BeerDto.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Crank")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("1235622")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(392)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        BeerDto beer3 = BeerDto.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Sunshine City")
                .beerStyle(BeerStyle.IPA)
                .upc("12356")
                .price(new BigDecimal("13.99"))
                .quantityOnHand(144)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);
    }

    @Override
    public Page<BeerDto> listBeers(Integer pageNumber, Integer pageSize) {
        // Notar que se devuelve un PageImpl
        return new PageImpl<>(new ArrayList<>(beerMap.values()));
    }

    @Override
    public Page<BeerDto> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle, Integer pageNumber, Integer pageSize) {
        return new PageImpl<>(beerMap.values().stream()
                .filter(beer -> beer.getBeerName().equals(beerName) && beer.getBeerStyle().equals(beerStyle))
                .toList());
    }

    @Override
    public Page<BeerDto> listBeersByNameContainingIgnoreCase(String beerName, Integer pageNumber, Integer pageSize) {
        return new PageImpl<>(beerMap.values().stream().filter(beer -> beer.getBeerName().equals(beerName)).toList());
    }

    @Override
    public Page<BeerDto> listBeersByStyle(BeerStyle beerStyle, Integer pageNumber, Integer pageSize) {
        return new PageImpl<>(beerMap.values().stream().filter(beer -> beer.getBeerStyle().equals(beerStyle)).toList());
    }

    @Override
    public Optional<BeerDto> getBeerById(UUID id) {
        log.debug("Get Beer by Id - in service. Id: " + id.toString());
        return Optional.of(beerMap.get(id));
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beer) {
        BeerDto savedBeer = BeerDto.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .version(beer.getVersion())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .quantityOnHand(beer.getQuantityOnHand())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .build();

        log.debug("Save New Beer: {}", savedBeer.toString());

        beerMap.put(savedBeer.getId(), savedBeer);

        return savedBeer;
    }

    @Override
    public Optional<BeerDto> updateBeerById(UUID beerId, BeerDto beer) {
        BeerDto existing = beerMap.get(beerId);
        existing.setBeerName(beer.getBeerName());
        existing.setPrice(beer.getPrice());
        existing.setUpc(beer.getUpc());
        existing.setQuantityOnHand(beer.getQuantityOnHand());
        existing.setUpdateDate(LocalDateTime.now());

        log.debug("Update Beer: {}", existing.toString());

        return Optional.of(existing);
    }

    @Override
    public Boolean deleteBeerById(UUID beerId) {
        log.debug("Delete Beer: {}", beerMap.get(beerId));

        beerMap.remove(beerId);

        // No nos complicamos la vida en la versión con Map, ya que realmente nos interesa la que trata con JPA.
        return true;
    }

    @Override
    public Optional<BeerDto> patchBeerById(UUID beerId, BeerDto beer) {
        // Estas son las reglas que se deberían cumplir en un PATCH.
        // Recordar que PATCH solo actualiza lo concreto que hay que actualizar (modificaciones parciales),
        // no es como el PUT, que actualiza to-do el objeto.
        // No se suele utilizar mucho.
        BeerDto existing = beerMap.get(beerId);

        if (StringUtils.hasText(beer.getBeerName())) {
            existing.setBeerName(beer.getBeerName());
        }

        if (beer.getBeerStyle() != null) {
            existing.setBeerStyle(beer.getBeerStyle());
        }

        if (beer.getPrice() != null) {
            existing.setPrice(beer.getPrice());
        }

        if (beer.getQuantityOnHand() != null) {
            existing.setQuantityOnHand(beer.getQuantityOnHand());
        }

        if (StringUtils.hasText(beer.getUpc())) {
            existing.setUpc(beer.getUpc());
        }

        existing.setUpdateDate(LocalDateTime.now());

        log.debug("Patch Beer: {}, {}", beerId, beer);

        return Optional.of(existing);
    }
}
