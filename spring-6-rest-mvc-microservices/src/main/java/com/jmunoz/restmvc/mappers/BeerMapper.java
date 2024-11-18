package com.jmunoz.restmvc.mappers;

import com.jmunoz.restmvc.entities.BeerAuditEntity;
import com.jmunoz.restmvc.entities.BeerEntity;
import guru.springframework.spring6restmvcapi.model.BeerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BeerMapper {

    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "beerOrderLines", ignore = true)
    BeerEntity beerDtoToBeerEntity(BeerDto dto);

    BeerDto beerEntityToBeerDto(BeerEntity beer);

    // Añadimos los mappers para BeerAuditEntity e ignoramos las propiedades indicadas
    // para que de verdad se pueda hacer el mapper entre ambas clases.
    @Mapping(target = "createdDateAudit", ignore = true)
    @Mapping(target = "auditId", ignore = true)
    @Mapping(target = "auditEventType", ignore = true)
    @Mapping(target = "principalName", ignore = true)
    BeerAuditEntity beerEntityToBeerAuditEntity(BeerEntity beer);
}