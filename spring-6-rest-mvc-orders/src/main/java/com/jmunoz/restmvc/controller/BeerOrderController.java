package com.jmunoz.restmvc.controller;

import com.jmunoz.restmvc.model.BeerOrderCreateDto;
import com.jmunoz.restmvc.model.BeerOrderDto;
import com.jmunoz.restmvc.model.BeerOrderUpdateDto;
import com.jmunoz.restmvc.services.BeerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @PostMapping(BEER_ORDER_PATH)
    public ResponseEntity<Void> saveBeerOrder(@Validated @RequestBody BeerOrderCreateDto beerOrderCreateDto) {
        BeerOrderDto savedBeerOrder = beerOrderService.saveBeerOrder(beerOrderCreateDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedBeerOrder.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(BEER_ORDER_PATH_ID)
    public ResponseEntity<BeerOrderDto> updateBeerOrder(@PathVariable("beerOrderId") UUID beerOrderId,
                                                        @Validated @RequestBody BeerOrderUpdateDto beerOrderUpdateDto) {

        return ResponseEntity.ok(beerOrderService.updateBeerOrder(beerOrderId, beerOrderUpdateDto));
    }
}
