package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    List<Customer> listCustomers();

    Customer getCustomerById(UUID id);
}
