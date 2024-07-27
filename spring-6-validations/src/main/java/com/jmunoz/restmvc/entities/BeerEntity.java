package com.jmunoz.restmvc.entities;

import com.jmunoz.restmvc.model.BeerStyle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

// No se recomienda @Data en Entities. Se cambia por @Setter y @Getter
//
// Para el uso de JPA Validation, es buena práctica indicar las mismas validaciones que haya en el DTO,
// para que sean consistentes.
//
// Un comportamiento por defecto de Hibernate es configurar las propiedades String como varchar(255),
// esto sobre to-do cuando crea la tabla.
// Vemos como se usa la anotación @Column sobre beerName para indicar que su longitud es de 50 y como afecta al test
// BeerRepositoryTest.
// La mejor práctica es usar la validación para que haga match el constraint de BBDD con el constraint de validación.
// En concreto se usa la anotación de Jakarta @Size, con lo que obtendremos un error de validación, en realidad
// otra excepción, pero preferible porque proviene de una validación.
// Para probar los dos tipos de excepciones, comentar y descomentar @Size sobre la propiedad beerName.
@Builder
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BeerEntity {

    // Dada esta configuración necesitamos hacer un mapeo de BBDD, de ahí el uso de @Column.
    // Esta anotación da pistas a Hibernate para generar el SQL que crea la tabla de BBDD H2 en memoria.
    @Id
    @UuidGenerator
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    private UUID id;

    // Empieza en 0 y con cada actualización se incrementa en 1.
    // Al hacer un objeto, Hibernate chequeará la versión en la BD y si son diferentes, lanzarán una excepción,
    // indicando que la data ha sido cambiada por otro proceso y tu proceso tiene datos obsoletos.
    // Es una forma de evitar actualizaciones perdidas.
    @Version
    private Integer version;

    @NotBlank
    @NotNull
    @Size(max = 50)
    @Column(length = 50)
    private String beerName;

    @NotNull
    private BeerStyle beerStyle;

    @NotBlank
    @NotNull
    @Size(max = 255)
    private String upc;

    private Integer quantityOnHand;

    @NotNull
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
