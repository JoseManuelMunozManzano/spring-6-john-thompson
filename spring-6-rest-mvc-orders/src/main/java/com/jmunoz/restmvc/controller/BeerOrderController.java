package com.jmunoz.restmvc.controller;

import com.jmunoz.restmvc.model.BeerOrderDto;
import com.jmunoz.restmvc.services.BeerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class BeerOrderController {

    public static final String BEER_ORDER_PATH = "/api/v1/beerorder";
    public static final String BEER_ORDER_PATH_ID = BEER_ORDER_PATH + "/{beerOrderId}";

    private final BeerOrderService beerOrderService;

    @GetMapping(value = BEER_ORDER_PATH)
    public Page<BeerOrderDto> listOrderBeers(@RequestParam(name = "pageNumber", required = false) Integer pageNumber,
                                             @RequestParam(name = "pageSize", required = false) Integer pageSize) {
        return beerOrderService.listBeerOrders(pageNumber, pageSize);
    }

    @GetMapping(value = BEER_ORDER_PATH_ID)
    public BeerOrderDto getBeerOrderById(@PathVariable("beerOrderId") UUID beerOrderId) {
        return beerOrderService.getBeerOrderById(beerOrderId).orElseThrow(NotFoundException::new);
    }
}
