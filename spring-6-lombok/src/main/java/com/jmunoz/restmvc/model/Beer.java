package com.jmunoz.restmvc.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

// Usando Project Lombok.
// Pulsar Cmd+Click Derecho para saber qué hace cada anotación.
//
// @Data
// Si vamos a Maven y seleccionamos del Lifecycle la opción Compile, veremos
// que en la carpeta target/classes/com/jmunoz/restmvc/model/Beer.class se ha generado
// los getter, setter, el constructor sin argumentos, equals() y hashMap() y el método toString().
//
// @Builder
// Implementa el patrón Builder para construir la instancia de un objeto.
@Builder
@Data
public class Beer {

    private UUID id;
    private Integer version;
    private String beerName;
    private BeerStyle beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
