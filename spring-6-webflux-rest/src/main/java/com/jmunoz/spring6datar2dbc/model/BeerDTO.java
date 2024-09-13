package com.jmunoz.spring6datar2dbc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Patrón DTO. En este caso los campos son los mismos que los de Beer.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDTO {

    private Integer id;
    private String beerName;
    private String beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
