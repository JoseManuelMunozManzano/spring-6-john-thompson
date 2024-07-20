package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    List<Customer> listCustomers();

    Customer getCustomerById(UUID id);

    Customer saveNewCustomer(Customer customer);

    void updateCustomerById(UUID id, Customer customer);

    void deleteCustomerById(UUID id);

    void patchCustomerById(UUID id, Customer customer);
}
