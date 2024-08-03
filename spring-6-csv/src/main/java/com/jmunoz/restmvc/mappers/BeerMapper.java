package com.jmunoz.restmvc.mappers;

import com.jmunoz.restmvc.entities.BeerEntity;
import com.jmunoz.restmvc.model.BeerDto;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    BeerEntity beerDtoToBeerEntity(BeerDto dto);

    BeerDto beerEntityToBeerDto(BeerEntity beer);
}