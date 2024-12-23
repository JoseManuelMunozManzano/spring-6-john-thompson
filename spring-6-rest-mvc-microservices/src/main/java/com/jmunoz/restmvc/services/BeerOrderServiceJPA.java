package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.controller.NotFoundException;
import com.jmunoz.restmvc.entities.*;
import com.jmunoz.restmvc.mappers.BeerOrderMapper;
import com.jmunoz.restmvc.repositories.BeerOrderRepository;
import com.jmunoz.restmvc.repositories.BeerRepository;
import com.jmunoz.restmvc.repositories.CustomerRepository;
import guru.springframework.spring6restmvcapi.events.OrderPlacedEvent;
import guru.springframework.spring6restmvcapi.model.BeerOrderCreateDto;
import guru.springframework.spring6restmvcapi.model.BeerOrderDto;
import guru.springframework.spring6restmvcapi.model.BeerOrderUpdateDto;
import guru.springframework.spring6restmvcapi.model.BeerStyle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class BeerOrderServiceJPA implements BeerOrderService {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;

    private final BeerRepository beerRepository;

    private final CustomerRepository customerRepository;

    // Para disparar el evento cuando se actualiza paymentAmount
    private final ApplicationEventPublisher applicationEventPublisher;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    @Override
    public Page listBeerOrders(Integer pageNumber, Integer pageSize) {

        log.info("listBeerOrders - in service");

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        return beerOrderRepository.findAll(pageRequest)
                .map(beerOrderMapper::beerOrderEntityToBeerOrderDto);
    }

    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else if (pageSize > 1000 || pageSize < 1) {
            queryPageSize = 1000;
        } else {
            queryPageSize = pageSize;
        }

        Sort sort = Sort.by(Sort.Order.asc("createdDate"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    @Override
    public Optional<BeerOrderDto> getBeerOrderById(UUID id) {

        log.info("getBeerOrderById - in service");

        return Optional.ofNullable(
                beerOrderMapper.beerOrderEntityToBeerOrderDto(beerOrderRepository.findById(id)
                        .orElse(null)));
    }

    @Override
    public BeerOrderDto saveBeerOrder(BeerOrderCreateDto beerOrderCreateDto) {

        CustomerEntity customer = customerRepository.findById(beerOrderCreateDto.getCustomerId())
                .orElseThrow(NotFoundException::new);

        Set<BeerOrderLineEntity> beerOrderLines = new HashSet<>();

        beerOrderCreateDto.getBeerOrderLines().forEach(beerOrderLine -> {
            beerOrderLines.add(BeerOrderLineEntity.builder()
                    .beer(beerRepository.findById(beerOrderLine.getBeerId()).orElseThrow(NotFoundException::new))
                    .orderQuantity(beerOrderLine.getOrderQuantity())
                    .build());
        });

        return beerOrderMapper.beerOrderEntityToBeerOrderDto(
                beerOrderRepository.save(BeerOrderEntity.builder()
                .customer(customer)
                .beerOrderLines(beerOrderLines)
                .customerRef(beerOrderCreateDto.getCustomerRef())
                .build()));
    }

    @Override
    public BeerOrderDto updateBeerOrder(UUID beerOrderId, BeerOrderUpdateDto beerOrderUpdateDto) {

        val beerOrder = beerOrderRepository.findById(beerOrderId).orElseThrow(NotFoundException::new);

        beerOrder.setCustomer(customerRepository.findById(beerOrderUpdateDto.getCustomerId()).orElseThrow(NotFoundException::new));
        beerOrder.setCustomerRef(beerOrderUpdateDto.getCustomerRef());

        beerOrderUpdateDto.getBeerOrderLines().forEach(beerOrderLine -> {
            if (beerOrderLine.getBeerId() != null) {
                val foundLine = beerOrder.getBeerOrderLines().stream()
                        .filter(beerOrderLine1 -> beerOrderLine1.getId().equals(beerOrderLine.getId()))
                        .findFirst().orElseThrow(NotFoundException::new);

                foundLine.setBeer(beerRepository.findById(beerOrderLine.getBeerId()).orElseThrow(NotFoundException::new));
                foundLine.setOrderQuantity(beerOrderLine.getOrderQuantity());
                foundLine.setQuantityAllocated(beerOrderLine.getQuantityAllocated());
            } else {
                beerOrder.getBeerOrderLines().add(BeerOrderLineEntity.builder()
                                .beer(createNewBeerOrHandleNewBeer())
                                .orderQuantity(beerOrderLine.getOrderQuantity())
                                .quantityAllocated(beerOrderLine.getQuantityAllocated())
                        .build());
            }
        });

        if (beerOrderUpdateDto.getBeerOrderShipment() != null && beerOrderUpdateDto.getBeerOrderShipment().getTrackingNumber() != null) {
            if (beerOrder.getBeerOrderShipment() == null) {
                beerOrder.setBeerOrderShipment(BeerOrderShipmentEntity.builder().trackingNumber(beerOrderUpdateDto.getBeerOrderShipment().getTrackingNumber()).build());
            } else {
                beerOrder.getBeerOrderShipment().setTrackingNumber(beerOrderUpdateDto.getBeerOrderShipment().getTrackingNumber());
            }
        }

        BeerOrderDto dto = beerOrderMapper.beerOrderEntityToBeerOrderDto(beerOrderRepository.save(beerOrder));

        // Aquí publicamos el evento
        if (beerOrderUpdateDto.getPaymentAmount() != null) {
            applicationEventPublisher.publishEvent(OrderPlacedEvent.builder()
                    .beerOrderDto(dto));
        }

        return dto;

    }

    @Override
    public void deleteBeerOrder(UUID beerOrderId) {

        BeerOrderEntity beerOrder = beerOrderRepository.findById(beerOrderId).orElseThrow(NotFoundException::new);

        beerOrderRepository.deleteById(beerOrderId);
    }

    private BeerEntity createNewBeerOrHandleNewBeer() {

        BeerEntity defaultBeer = BeerEntity.builder()
                .beerName("Default Beer")
                .beerStyle(BeerStyle.IPA)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .upc("123")
                .build();

        return defaultBeer;
    }
}
