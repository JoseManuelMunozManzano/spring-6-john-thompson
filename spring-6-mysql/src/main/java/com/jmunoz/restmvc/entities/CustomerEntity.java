package com.jmunoz.restmvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

// No se recomienda @Data en Entities. Se cambia por @Setter y @Getter
@Builder
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="customer")
public class CustomerEntity {

    // Dada esta configuración necesitamos hacer un mapeo de BBDD, de ahí el uso de @Column.
    // Esta anotación da pistas a Hibernate para generar el SQL que crea la tabla de BBDD H2 en memoria.
    //
    // Se indica varchar(36) para corregir el error al intentar generarse la tabla para MySQL (H2 funciona sin esto)
    // El error es que se genera un Script SQL para crear la tabla que indica el campo id como VARCHAR sin la
    // longitud, lo que da error en MySQL.
    //
    // Con @JdbcTypeCode() hacemos un mapeo del UUID de binary a carácteres. Por defecto lo está escribiendo como binary
    // lo que da un error en MySQL (no da error en H2)
    @Id
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
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
