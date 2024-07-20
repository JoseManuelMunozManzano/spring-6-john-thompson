package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<UUID, Customer> customerMap;

    public CustomerServiceImpl() {
        customerMap = new HashMap<>();

        Customer jm = Customer.builder()
                .id(UUID.randomUUID())
                .name("José Manuel")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer adri = Customer.builder()
                .id(UUID.randomUUID())
                .name("Adriana")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Customer marina = Customer.builder()
                .id(UUID.randomUUID())
                .name("Marina")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(jm.getId(), jm);
        customerMap.put(adri.getId(), adri);
        customerMap.put(marina.getId(), marina);
    }

    @Override
    public List<Customer> listCustomers() {
        log.debug("Get All Customers");
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Customer getCustomerById(UUID id) {
        log.debug("Get Customer by Id - in service. Id: {}", id.toString());
        return customerMap.get(id);
    }
}