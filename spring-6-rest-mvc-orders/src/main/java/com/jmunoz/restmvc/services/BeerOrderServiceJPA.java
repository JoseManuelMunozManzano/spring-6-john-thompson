package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.mappers.BeerOrderMapper;
import com.jmunoz.restmvc.model.BeerOrderDto;
import com.jmunoz.restmvc.repositories.BeerOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class BeerOrderServiceJPA implements BeerOrderService {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;

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
}
