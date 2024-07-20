package com.jmunoz.restmvc.controller;

import com.jmunoz.restmvc.model.Customer;
import com.jmunoz.restmvc.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    List<Customer> testCustomers;

    @BeforeEach
    void setUp() {
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

        testCustomers = Arrays.asList(jm, adri, marina);
    }

    @Test
    void listCustomers() throws Exception {
        given(customerService.listCustomers()).willReturn(testCustomers);

        mockMvc.perform(get("/api/v1/customer")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getCustomerById() throws Exception {
        given(customerService.getCustomerById(testCustomers.getFirst().getId())).willReturn(testCustomers.getFirst());

        mockMvc.perform(get("/api/v1/customer/" + testCustomers.getFirst().getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testCustomers.getFirst().getId().toString())))
                .andExpect(jsonPath("$.name", is(testCustomers.getFirst().getName())));
    }
}