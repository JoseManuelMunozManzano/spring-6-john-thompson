package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.model.BeerOrderCreateDto;
import com.jmunoz.restmvc.model.BeerOrderDto;
import com.jmunoz.restmvc.model.BeerOrderUpdateDto;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface BeerOrderService {

    Page<BeerOrderDto> listBeerOrders(Integer pageNumber, Integer pageSize);

    Optional<BeerOrderDto> getBeerOrderById(UUID id);

    BeerOrderDto saveBeerOrder(BeerOrderCreateDto beerOrderCreateDto);

    BeerOrderDto updateBeerOrder(UUID beerOrderId, BeerOrderUpdateDto beerOrderUpdateDto);

    void deleteBeerOrder(UUID beerOrderId);
}
