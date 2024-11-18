package com.jmunoz.restmvc.mappers;

import com.jmunoz.restmvc.entities.BeerOrderEntity;
import com.jmunoz.restmvc.entities.BeerOrderLineEntity;
import com.jmunoz.restmvc.entities.BeerOrderShipmentEntity;
import guru.springframework.spring6restmvcapi.model.BeerOrderDto;
import guru.springframework.spring6restmvcapi.model.BeerOrderLineDto;
import guru.springframework.spring6restmvcapi.model.BeerOrderShipmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BeerOrderMapper {

    BeerOrderEntity beerOrderDtoToBeerOrderEntity(BeerOrderDto dto);

    BeerOrderDto beerOrderEntityToBeerOrderDto(BeerOrderEntity beerOrder);

    @Mapping(target = "beerOrder", ignore = true)
    BeerOrderLineEntity beerOrderLineDtoToBeerOrderLineEntity(BeerOrderLineDto dto);

    BeerOrderLineDto beerOrderLineEntityToBeerOrderLineDto(BeerOrderLineEntity beerOrderLine);

    @Mapping(target = "beerOrder", ignore = true)
    BeerOrderShipmentEntity beerOrderShipmentDtoToBeerOrderShipmentEntity(BeerOrderShipmentDto dto);

    BeerOrderShipmentDto beerOrderShipmenEntityToBeerOrderShipmentDto(BeerOrderShipmentEntity beerOrderShipment);
}
