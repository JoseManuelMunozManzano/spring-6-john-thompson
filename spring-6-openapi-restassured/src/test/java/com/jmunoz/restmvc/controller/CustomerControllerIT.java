package com.jmunoz.restmvc.controller;

import com.jmunoz.restmvc.entities.CustomerEntity;
import com.jmunoz.restmvc.mappers.CustomerMapper;
import com.jmunoz.restmvc.model.CustomerDto;
import com.jmunoz.restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Test
    void testListCustomers() {
        List<CustomerDto> dtos = customerController.listCustomers();

        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        customerRepository.deleteAll();

        List<CustomerDto> dtos = customerController.listCustomers();

        assertThat(dtos.size()).isEqualTo(0);
    }

    @Test
    void testGetById() {
        CustomerEntity customer = customerRepository.findAll().getFirst();

        CustomerDto dto = customerController.getCustomerById(customer.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    void testCustomerIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            // 99.999% de seguridad que genera un UUID distinto a los que tenemos en los datos de inicio (bootstrap)
            customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void saveNewCustomerTest() {
        CustomerDto customerDto = CustomerDto.builder()
                .name("New Customer")
                .build();

        ResponseEntity<CustomerDto> responseEntity = customerController.handlePost(customerDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        CustomerEntity customerEntity = customerRepository.findById(savedUUID).get();
        assertThat(customerEntity).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void updateExistingCustomer() {
        CustomerEntity customerEntity = customerRepository.findAll().getFirst();
        CustomerDto customerDto = customerMapper.customerEntityToCustomerDto(customerEntity);
        customerDto.setId(null);
        customerDto.setVersion(null);
        final String customerName = "UPDATED";
        customerDto.setName(customerName);

        ResponseEntity<CustomerDto> responseEntity = customerController.updateById(customerEntity.getId(), customerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        CustomerEntity updatedCustomer = customerRepository.findById(customerEntity.getId()).get();
        assertThat(updatedCustomer.getName()).isEqualTo(customerName);
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.updateById(UUID.randomUUID(), CustomerDto.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void deleteByIdFound() {
        CustomerEntity customerEntity = customerRepository.findAll().getFirst();

        ResponseEntity<CustomerDto> responseEntity = customerController.deleteById(customerEntity.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(customerRepository.findById(customerEntity.getId())).isEmpty();
    }

    @Test
    void testDeleteNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.deleteById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void patchExistingCustomer() {
        CustomerEntity customerEntity = customerRepository.findAll().getFirst();
        CustomerDto customerDto = customerMapper.customerEntityToCustomerDto(customerEntity);
        customerDto.setId(null);
        customerDto.setVersion(null);
        final String customerName = "UPDATED";
        customerDto.setName(customerName);

        ResponseEntity<CustomerDto> responseEntity = customerController.patchById(customerEntity.getId(), customerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        CustomerEntity updatedCustomer = customerRepository.findById(customerEntity.getId()).get();
        assertThat(updatedCustomer.getName()).isEqualTo(customerName);
    }

    @Test
    void testPatchNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.patchById(UUID.randomUUID(), CustomerDto.builder().build());
        });
    }
}