package com.jmunoz.spring6datar2dbc.mappers;

import com.jmunoz.spring6datar2dbc.domain.Customer;
import com.jmunoz.spring6datar2dbc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO dto);

    CustomerDTO customerToCustomerDto(Customer customer);
}
