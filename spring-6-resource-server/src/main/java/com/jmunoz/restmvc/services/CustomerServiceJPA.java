package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.mappers.CustomerMapper;
import com.jmunoz.restmvc.model.CustomerDto;
import com.jmunoz.restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

// Como ahora vamos a tener dos implementaciones de CustomerService, este que usa JPA lo hacemos @Primary
@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService {

    // Usamos el repositorio en conjunción con el mapper.
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Cacheable(cacheNames = "customerListCache")
    @Override
    public List<CustomerDto> listCustomers() {
        log.info("List Customers - in service");

        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerEntityToCustomerDto)
                .toList();
    }

    @Cacheable(cacheNames = "customerCache")
    @Override
    public Optional<CustomerDto> getCustomerById(UUID id) {
        log.info("Get Customer by Id - in service");

        return Optional.ofNullable(customerMapper
                .customerEntityToCustomerDto(customerRepository.findById(id).orElse(null)));
    }

    @Override
    public CustomerDto saveNewCustomer(CustomerDto customer) {
        return customerMapper.customerEntityToCustomerDto(
                customerRepository.save(customerMapper.customerDtoToCustomerEntity(customer)));
    }

    @Override
    public Optional<CustomerDto> updateCustomerById(UUID id, CustomerDto customer) {
        AtomicReference<Optional<CustomerDto>> atomicReference = new AtomicReference<>();

        customerRepository.findById(id).ifPresentOrElse(foundCustomer -> {
            foundCustomer.setName(customer.getName());

            atomicReference.set(Optional.of(customerMapper
                    .customerEntityToCustomerDto(customerRepository.save(foundCustomer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public Boolean deleteCustomerById(UUID id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Override
    public Optional<CustomerDto> patchCustomerById(UUID id, CustomerDto customer) {
        AtomicReference<Optional<CustomerDto>> atomicReference = new AtomicReference<>();

        customerRepository.findById(id).ifPresentOrElse(foundCustomer -> {
            if (StringUtils.hasText(customer.getName())) {
                foundCustomer.setName(customer.getName());
            }

            atomicReference.set(Optional.of(customerMapper
                    .customerEntityToCustomerDto(customerRepository.save(foundCustomer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
