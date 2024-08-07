package com.jmunoz.restmvc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class BeerDto {

    private UUID id;
    private Integer version;

    // Añadimos validaciones usando Jakarta Bean Validation.
    // Se podría usar solo @NotBlank, pero de forma explícita quiero indicar que además no puede ser nulo.
    // Estas validaciones, por sí solas, no funcionan si no se indica, en el controller (request), la anotación @Validated.
    //
    // Indicar que BeerEntity tiene validaciones de longitud de nombre de máximo 50 caracteres que aquí no se ha indicado,
    // y debería haberse hecho porque deberían hacer match. No se ha hecho porque se ha querido mostrar violaciones de JPA
    // hacia arriba (al controlador) enviando al cliente un mensaje.
    @NotBlank
    @NotNull
    private String beerName;

    // No se utiliza el @NotBlank por ser un enum
    @NotNull
    private BeerStyle beerStyle;

    @NotBlank
    @NotNull
    private String upc;

    private Integer quantityOnHand;

    @NotNull
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
