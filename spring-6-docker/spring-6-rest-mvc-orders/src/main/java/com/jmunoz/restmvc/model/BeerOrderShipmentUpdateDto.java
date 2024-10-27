package com.jmunoz.restmvc.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Se añade @AllArgsConstructor y @NoArgsConstructor para corregir un problema del test
// BeerOrderControllerIT.testUpdateBeerOrder, en concreto un error JSON parse, que indica
// que no se puede construir una instancia de este DTO.
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeerOrderShipmentUpdateDto {

    // No especificamos la property id porque para BeerOrderEntity hay una relación 1 a 1
    // con BeerOrderShipment, es decir, solo hay una, por lo que no tenemos que preocuparnos
    // por ella.

    @NotBlank
    private String trackingNumber;
}
