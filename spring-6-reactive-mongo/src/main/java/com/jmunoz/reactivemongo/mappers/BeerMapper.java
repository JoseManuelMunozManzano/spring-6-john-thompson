package com.jmunoz.reactivemongo.mappers;

import com.jmunoz.reactivemongo.domain.Beer;
import com.jmunoz.reactivemongo.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    BeerDTO beerToBeerDto(Beer beer);

    Beer beerDtoToBeer(BeerDTO beerDTO);
}
