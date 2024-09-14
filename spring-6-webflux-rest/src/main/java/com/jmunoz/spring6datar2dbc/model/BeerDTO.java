package com.jmunoz.spring6datar2dbc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Patrón DTO. En este caso los campos son los mismos que los de Beer.
// Ponemos algunas validaciones, pero me enfoco más en ver como funciona en vez de que estas validaciones
// sean muy sólidas.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDTO {

    private Integer id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String beerName;

    @NotBlank
    @Size(min = 1, max = 255)
    private String beerStyle;

    @Size(max = 25)
    private String upc;

    private Integer quantityOnHand;
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
