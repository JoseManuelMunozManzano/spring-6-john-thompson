package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.mappers.CustomerMapper;
import com.jmunoz.restmvc.model.CustomerDto;
import com.jmunoz.restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Como ahora vamos a tener dos implementaciones de CustomerService, este que usa JPA lo hacemos @Primary
@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    // Usamos el repositorio en conjunción con el mapper.
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDto> listCustomers() {
        return List.of();
    }

    @Override
    public Optional<CustomerDto> getCustomerById(UUID id) {
        return Optional.empty();
    }

    @Override
    public CustomerDto saveNewCustomer(CustomerDto customer) {
        return null;
    }

    @Override
    public void updateCustomerById(UUID id, CustomerDto customer) {

    }

    @Override
    public void deleteCustomerById(UUID id) {

    }

    @Override
    public void patchCustomerById(UUID id, CustomerDto customer) {

    }
}
