package com.jmunoz.restmvc.mappers;

import com.jmunoz.restmvc.entities.CustomerEntity;
import guru.springframework.spring6restmvcapi.model.CustomerDto;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    CustomerEntity customerDtoToCustomerEntity(CustomerDto dto);

    CustomerDto customerEntityToCustomerDto(CustomerEntity customer);
}
