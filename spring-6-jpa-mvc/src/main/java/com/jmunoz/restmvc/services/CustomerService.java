package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.model.CustomerDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<CustomerDto> listCustomers();

    Optional<CustomerDto> getCustomerById(UUID id);

    CustomerDto saveNewCustomer(CustomerDto customer);

    void updateCustomerById(UUID id, CustomerDto customer);

    void deleteCustomerById(UUID id);

    void patchCustomerById(UUID id, CustomerDto customer);
}
