package com.jmunoz.restmvc.controller;

import com.jmunoz.restmvc.model.Customer;
import com.jmunoz.restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> listCustomers() {
        log.debug("List Customers - In Controller");
        return customerService.listCustomers();
    }

    @RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("customerId")UUID id) {
        log.debug("Get Customer By Id - In Controller. Id = {}", id.toString());
        return customerService.getCustomerById(id);
    }
}
