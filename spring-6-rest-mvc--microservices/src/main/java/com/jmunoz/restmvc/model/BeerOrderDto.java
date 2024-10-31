package com.jmunoz.restmvc.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Builder
@Data
public class BeerOrderDto {
    private UUID id;
    private Long version;
    private Timestamp createdDate;
    private Timestamp lastModifiedDate;
    private String customerRef;
    private CustomerDto customer;
    private BigDecimal paymentAmount;
    private Set<BeerOrderLineDto> beerOrderLines;
    private BeerOrderShipmentDto beerOrderShipment;
}
