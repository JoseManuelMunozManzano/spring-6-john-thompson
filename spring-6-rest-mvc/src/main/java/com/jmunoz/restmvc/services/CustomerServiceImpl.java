package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    @Override
    public Customer saveNewCustomer(Customer customer) {
        Customer savedCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .version(customer.getVersion())
                .name(customer.getName())
                .build();

        log.debug("Save New Customer: {}", savedCustomer.toString());

        customerMap.put(savedCustomer.getId(), savedCustomer);

        return savedCustomer;
    }

    @Override
    public void updateCustomerById(UUID id, Customer customer) {
        Customer existing = customerMap.get(id);
        existing.setName(customer.getName());
        existing.setLastModifiedDate(LocalDateTime.now());

        log.debug("Update Customer: {}", existing.toString());
    }

    @Override
    public void deleteCustomerById(UUID id) {
        log.debug("Delete Customer: {}", customerMap.get(id));

        customerMap.remove(id);
    }

    @Override
    public void patchCustomerById(UUID id, Customer customer) {
        Customer existing = customerMap.get(id);

        if (StringUtils.hasText(customer.getName())) {
            existing.setName(customer.getName());
        }

        existing.setLastModifiedDate(LocalDateTime.now());

        log.debug("Patch Customer: {}, {}", id, customer);
    }
}
