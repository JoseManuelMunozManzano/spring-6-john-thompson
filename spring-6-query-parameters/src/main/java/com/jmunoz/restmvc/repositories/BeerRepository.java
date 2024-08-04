package com.jmunoz.restmvc.repositories;

import com.jmunoz.restmvc.entities.BeerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<BeerEntity, UUID> {

    // Uso de Query Methods para devolver lista de beers
    List<BeerEntity> findAllByBeerNameIsLikeIgnoreCase(String beerName);
}
