package com.jmunoz.restmvc.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BeerOrderLineCreateDto {

    @NotNull
    private UUID beerId;

    @Min(value = 1, message = "Quantity On Hand must be greater than 0")
    private Integer orderQuantity;
}
