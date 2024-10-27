package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.model.CustomerDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<CustomerDto> listCustomers();

    Optional<CustomerDto> getCustomerById(UUID id);

    CustomerDto saveNewCustomer(CustomerDto customer);

    Optional<CustomerDto> updateCustomerById(UUID id, CustomerDto customer);

    Boolean deleteCustomerById(UUID id);

    Optional<CustomerDto> patchCustomerById(UUID id, CustomerDto customer);
}
