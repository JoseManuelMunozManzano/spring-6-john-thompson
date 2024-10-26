package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.controller.NotFoundException;
import com.jmunoz.restmvc.entities.BeerOrderEntity;
import com.jmunoz.restmvc.entities.BeerOrderLineEntity;
import com.jmunoz.restmvc.entities.CustomerEntity;
import com.jmunoz.restmvc.mappers.BeerOrderMapper;
import com.jmunoz.restmvc.model.BeerOrderCreateDto;
import com.jmunoz.restmvc.model.BeerOrderDto;
import com.jmunoz.restmvc.repositories.BeerOrderRepository;
import com.jmunoz.restmvc.repositories.BeerRepository;
import com.jmunoz.restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    @Override
    public Page<BeerOrderDto> listBeerOrders(Integer pageNumber, Integer pageSize) {

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
}
