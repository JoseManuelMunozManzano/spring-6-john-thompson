package com.jmunoz.restmvc.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

// No se recomienda @Data en Entities. Se cambia por @Setter y @Getter
@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEntity {

    @Id
    private UUID id;

    // Empieza en 0 y con cada actualización se incrementa en 1.
    // Al hacer un objeto, Hibernate chequeará la versión en la BD y si son diferentes, lanzarán una excepción,
    // indicando que la data ha sido cambiada por otro proceso y tu proceso tiene datos obsoletos.
    // Es una forma de evitar actualizaciones perdidas.
    @Version
    private Integer version;

    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
