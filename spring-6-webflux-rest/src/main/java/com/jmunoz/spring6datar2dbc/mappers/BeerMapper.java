package com.jmunoz.spring6datar2dbc.mappers;

import com.jmunoz.spring6datar2dbc.domain.Beer;
import com.jmunoz.spring6datar2dbc.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer BeerDtoToBeer(BeerDTO dto);

    BeerDTO BeerToBeerDTO(Beer beer);

}
