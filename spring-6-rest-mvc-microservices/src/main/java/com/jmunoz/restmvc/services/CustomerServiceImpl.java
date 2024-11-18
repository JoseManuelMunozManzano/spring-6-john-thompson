package com.jmunoz.restmvc.services;

import guru.springframework.spring6restmvcapi.model.CustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<UUID, CustomerDto> customerMap;

    public CustomerServiceImpl() {
        customerMap = new HashMap<>();

        CustomerDto jm = CustomerDto.builder()
                .id(UUID.randomUUID())
                .name("José Manuel")
                .version(1)
                .createdDate(LocalDateTime.now())
                .build();

        CustomerDto adri = CustomerDto.builder()
                .id(UUID.randomUUID())
                .name("Adriana")
                .version(1)
                .createdDate(LocalDateTime.now())
                .build();

        CustomerDto marina = CustomerDto.builder()
                .id(UUID.randomUUID())
                .name("Marina")
                .version(1)
                .createdDate(LocalDateTime.now())
                .build();

        customerMap.put(jm.getId(), jm);
        customerMap.put(adri.getId(), adri);
        customerMap.put(marina.getId(), marina);
    }

    @Override
    public List<CustomerDto> listCustomers() {
        log.debug("Get All Customers");
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDto> getCustomerById(UUID id) {
        log.debug("Get Customer by Id - in service. Id: {}", id.toString());
        return Optional.of(customerMap.get(id));
    }

    @Override
    public CustomerDto saveNewCustomer(CustomerDto customer) {
        CustomerDto savedCustomer = CustomerDto.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .version(customer.getVersion())
                .name(customer.getName())
                .build();

        log.debug("Save New Customer: {}", savedCustomer.toString());

        customerMap.put(savedCustomer.getId(), savedCustomer);

        return savedCustomer;
    }

    @Override
    public Optional<CustomerDto> updateCustomerById(UUID id, CustomerDto customer) {
        CustomerDto existing = customerMap.get(id);
        existing.setName(customer.getName());

        log.debug("Update Customer: {}", existing.toString());

        return Optional.of(existing);
    }

    @Override
    public Boolean deleteCustomerById(UUID id) {
        log.debug("Delete Customer: {}", customerMap.get(id));

        customerMap.remove(id);
        return true;
    }

    @Override
    public Optional<CustomerDto> patchCustomerById(UUID id, CustomerDto customer) {
        CustomerDto existing = customerMap.get(id);

        if (StringUtils.hasText(customer.getName())) {
            existing.setName(customer.getName());
        }

        log.debug("Patch Customer: {}, {}", id, customer);

        return Optional.of(existing);
    }
}
