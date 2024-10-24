package com.jmunoz.restmvc.model;

import jakarta.validation.constraints.Min;
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

    @Min(value = 1, message = "Quantity On Hand must be greater than 0")
    private Integer orderQuantity;

    private Integer quantityAllocated;

    private Timestamp createdDate;
    private Timestamp lastModifiedDate;
}
