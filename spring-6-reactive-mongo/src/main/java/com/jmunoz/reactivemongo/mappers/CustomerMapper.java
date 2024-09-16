package com.jmunoz.reactivemongo.mappers;

import com.jmunoz.reactivemongo.domain.Customer;
import com.jmunoz.reactivemongo.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    CustomerDTO customerToCustomerDto(Customer customer);

    Customer customerDtoToCustomer(CustomerDTO customerDTO);
}
