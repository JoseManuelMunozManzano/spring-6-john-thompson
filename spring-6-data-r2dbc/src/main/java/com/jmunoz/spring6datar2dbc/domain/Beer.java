package com.jmunoz.spring6datar2dbc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Esta es nuestra implementación básica del POJO que utilizaremos para persistir y recuperar data de la BD usando
// el stack reactivo.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beer {

    // Notar que ahora, @Id, en vez de importar de JPA, importa de springframework.
    // Esto es porque no usamos JPA.
    @Id
    private Integer id;
    private String beerName;
    private String beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

}
