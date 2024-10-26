package com.jmunoz.restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmunoz.restmvc.model.*;
import com.jmunoz.restmvc.repositories.BeerOrderRepository;
import com.jmunoz.restmvc.repositories.BeerRepository;
import com.jmunoz.restmvc.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    // Se usa el Transactional para que Hibernate tenga una sesión y pueda hacer la carga perezosa sobre
    // beerOrderLines.
    @Transactional
    @Test
    void testUpdateBeerOrder() throws Exception {
        val beerOrder = beerOrderRepository.findAll().getFirst();

        Set<BeerOrderLineUpdateDto> beerOrderLineUpdateDtos = new HashSet<>();

        // Necesario la anotación @Transactional
        beerOrder.getBeerOrderLines().forEach(beerOrderLine -> {
            beerOrderLineUpdateDtos.add(BeerOrderLineUpdateDto.builder()
                            .id(beerOrderLine.getId())
                            .beerId(beerOrderLine.getBeer().getId())
                            .orderQuantity(beerOrderLine.getOrderQuantity())
                            .quantityAllocated(beerOrderLine.getQuantityAllocated())
                    .build());
        });

        val beerOrderUpdate = BeerOrderUpdateDto.builder()
                .customerRef("New Customer Ref")
                .customerId(beerOrder.getCustomer().getId())
                .beerOrderLines(beerOrderLineUpdateDtos)
                .beerOrderShipment(BeerOrderShipmentUpdateDto.builder()
                        .trackingNumber("123456")
                        .build())
                .build();

        mockMvc.perform(put(BeerOrderController.BEER_ORDER_PATH_ID, beerOrder.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerOrderUpdate))
                        .with(BeerControllerTest.jwtRequestPostProcessor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerRef", is("New Customer Ref")));
    }

    @Test
    void testDeleteBeerOrder() throws Exception {
        val beerOrder = beerOrderRepository.findAll().getFirst();

        mockMvc.perform(delete(BeerOrderController.BEER_ORDER_PATH_ID, beerOrder.getId())
                .with(BeerControllerTest.jwtRequestPostProcessor))
                .andExpect(status().isOk());

        assertTrue(beerOrderRepository.findById(beerOrder.getId()).isEmpty());

        mockMvc.perform(delete(BeerOrderController.BEER_ORDER_PATH_ID, beerOrder.getId())
                        .with(BeerControllerTest.jwtRequestPostProcessor))
                .andExpect(status().isNotFound());
    }
}