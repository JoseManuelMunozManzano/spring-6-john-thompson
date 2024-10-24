package com.jmunoz.restmvc.model;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Builder
@Data
public class BeerOrderLineDto {
    private UUID id;
    private Long version;
    private BeerDto beer;
    private Integer orderQuantity;
    private Integer quantityAllocated;
    private Timestamp createdDate;
    private Timestamp lastModifiedDate;
}
