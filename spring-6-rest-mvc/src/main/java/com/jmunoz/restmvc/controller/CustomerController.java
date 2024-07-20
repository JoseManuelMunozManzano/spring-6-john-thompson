package com.jmunoz.restmvc.controller;

import com.jmunoz.restmvc.model.Customer;
import com.jmunoz.restmvc.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    // Con solo una propiedad (name) actualizable, Customer no es un buen candidato
    // a hacer un PATCH. Se hace por motivos didácticos.
    @PatchMapping("{customerId}")
    public ResponseEntity<Customer> patchById(@PathVariable("customerId") UUID id, @RequestBody Customer customer) {
        customerService.patchCustomerById(id, customer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{customerId}")
    public ResponseEntity<Customer> deleteById(@PathVariable("customerId") UUID id) {
        customerService.deleteCustomerById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{customerId}")
    public ResponseEntity<Customer> updateById(@PathVariable("customerId") UUID id, @RequestBody Customer customer) {
        customerService.updateCustomerById(id, customer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Customer> handlePost(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.saveNewCustomer(customer);

        // Otra forma de crear la property location en los headers.
        // Es más bonita la forma mostrada en BeerController.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedCustomer.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> listCustomers() {
        log.debug("List Customers - In Controller");
        return customerService.listCustomers();
    }

    @RequestMapping(value = "{customerId}", method = RequestMethod.GET)
    public Customer getCustomerById(@PathVariable("customerId")UUID id) {
        log.debug("Get Customer By Id - In Controller. Id = {}", id.toString());
        return customerService.getCustomerById(id);
    }
}
