package com.jmunoz.restmvc.controller;

import com.jmunoz.restmvc.services.CustomerService;
import guru.springframework.spring6restmvcapi.model.CustomerDto;
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
    public ResponseEntity<CustomerDto> patchById(@PathVariable("customerId") UUID id, @RequestBody CustomerDto customer) {
        if(customerService.patchCustomerById(id, customer).isEmpty()) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{customerId}")
    public ResponseEntity<CustomerDto> deleteById(@PathVariable("customerId") UUID id) {
        if (!customerService.deleteCustomerById(id)) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{customerId}")
    public ResponseEntity<CustomerDto> updateById(@PathVariable("customerId") UUID id, @RequestBody CustomerDto customer) {
        if (customerService.updateCustomerById(id, customer).isEmpty()) {
            throw  new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<CustomerDto> handlePost(@RequestBody CustomerDto customer) {
        CustomerDto savedCustomer = customerService.saveNewCustomer(customer);

        // Otra forma de crear la property location en los headers.
        // Es más bonita la forma mostrada en BeerController.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + savedCustomer.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<CustomerDto> listCustomers() {
        log.debug("List Customers - In Controller");
        return customerService.listCustomers();
    }

    @RequestMapping(value = "{customerId}", method = RequestMethod.GET)
    public CustomerDto getCustomerById(@PathVariable("customerId")UUID id) {
        log.debug("Get Customer By Id - In Controller. Id = {}", id.toString());
        return customerService.getCustomerById(id).orElseThrow(NotFoundException::new);
    }
}
