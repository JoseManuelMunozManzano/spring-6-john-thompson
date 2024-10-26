package com.jmunoz.restmvc.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BeerOrderShipmentUpdateDto {

    // No especificamos la property id porque para BeerOrderEntity hay una relación 1 a 1
    // con BeerOrderShipment, es decir, solo hay una, por lo que no tenemos que preocuparnos
    // por ella.

    @NotBlank
    private String trackingNumber;
}
