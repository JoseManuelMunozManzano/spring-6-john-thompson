package com.jmunoz.restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmunoz.restmvc.model.BeerOrderCreateDto;
import com.jmunoz.restmvc.model.BeerOrderLineCreateDto;
import com.jmunoz.restmvc.repositories.BeerOrderRepository;
import com.jmunoz.restmvc.repositories.BeerRepository;
import com.jmunoz.restmvc.repositories.CustomerRepository;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class BeerOrderControllerIT {

    @Autowired
    WebApplicationContext wac;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testListBeerOrders() throws Exception {
        mockMvc.perform(get(BeerOrderController.BEER_ORDER_PATH)
                .with(BeerControllerTest.jwtRequestPostProcessor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", greaterThan(0)));

    }

    @Test
    void testGetBeerOrderById() throws Exception {
        val beerOrder = beerOrderRepository.findAll().getFirst();

        mockMvc.perform(get(BeerOrderController.BEER_ORDER_PATH_ID, beerOrder.getId())
                .with(BeerControllerTest.jwtRequestPostProcessor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(beerOrder.getId().toString())));

    }

    @Test
    void testCreateBeerOrder() throws Exception {
        val customer = customerRepository.findAll().getFirst();
        val beer = beerRepository.findAll().getFirst();

        val beerOrderLineCreateDto = BeerOrderLineCreateDto.builder()
                .beerId(beer.getId())
                .orderQuantity(2)
                .build();

        val beerOrderCreateDto = BeerOrderCreateDto.builder()
                .customerId(customer.getId())
                .beerOrderLines(Set.of(beerOrderLineCreateDto))
                .customerRef("123")
                .build();

        mockMvc.perform(post(BeerOrderController.BEER_ORDER_PATH)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(beerOrderCreateDto))
                .with(BeerControllerTest.jwtRequestPostProcessor))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }
}