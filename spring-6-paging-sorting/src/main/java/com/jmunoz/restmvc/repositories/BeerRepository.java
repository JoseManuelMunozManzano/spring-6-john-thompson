package com.jmunoz.restmvc.repositories;

import com.jmunoz.restmvc.entities.BeerEntity;
import com.jmunoz.restmvc.model.BeerStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerRepository extends JpaRepository<BeerEntity, UUID> {

    // Uso de Query Methods para devolver lista de beers
    //
    // Refactorizado para aceptar un objeto de tipo Pageable y devolver un objeto Page (en vez de List), para la paginación
    Page<BeerEntity> findAllByBeerNameIsLikeIgnoreCase(String beerName, Pageable pageable);

    Page<BeerEntity> findAllByBeerStyle(BeerStyle beerStyle, Pageable pageable);

    Page<BeerEntity> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle, Pageable pageable);
}
