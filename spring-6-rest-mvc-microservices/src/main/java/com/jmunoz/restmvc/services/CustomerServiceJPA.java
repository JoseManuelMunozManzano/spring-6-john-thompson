package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.mappers.CustomerMapper;
import com.jmunoz.restmvc.repositories.CustomerRepository;
import guru.springframework.spring6restmvcapi.model.CustomerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
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

    // Para limpiar la caché
    private final CacheManager cacheManager;

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

        if (cacheManager.getCache("customerListCache") != null) {
            cacheManager.getCache("customerListCache").clear();
        }

        return customerMapper.customerEntityToCustomerDto(
                customerRepository.save(customerMapper.customerDtoToCustomerEntity(customer)));
    }

    @Override
    public Optional<CustomerDto> updateCustomerById(UUID id, CustomerDto customer) {

        clearCache(id);

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

    private void clearCache(UUID id) {
        if (cacheManager.getCache("customerCache") != null) {
            cacheManager.getCache("customerCache").evict(id);
        }

        if (cacheManager.getCache("customerListCache") != null) {
            cacheManager.getCache("customerListCache").clear();
        }
    }

    @Override
    public Boolean deleteCustomerById(UUID id) {

        clearCache(id);

        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Override
    public Optional<CustomerDto> patchCustomerById(UUID id, CustomerDto customer) {

        clearCache(id);

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
