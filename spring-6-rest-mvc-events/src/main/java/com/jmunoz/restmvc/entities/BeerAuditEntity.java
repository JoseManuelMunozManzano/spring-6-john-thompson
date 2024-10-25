package com.jmunoz.restmvc.entities;

import com.jmunoz.restmvc.model.BeerStyle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

// Para nuestro ejemplo de Eventos, esta es la entidad del log de auditoría.
// Va a ser igual a BeerEntity
//
// Tal y como está, no se actualiza MySql, pero si ejecutamos contra la BD H2, esta tabla se creará automáticamente.
// En una clase posterior se creará la tabla con SQL.
@Builder
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="beer_audit")
public class BeerAuditEntity {

    // Este ID es el id de esta tabla de auditoría
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID auditId;

    // Quitamos muchos de los constraints que había en BeerEntity porque quiero que sea muy flexible.
    // Para una tabla de auditoría esto es lo mejor.
    // No vamos a persistir las líneas de órdenes relacionadas ni las categorías.
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    private Integer version;

    @Size(max = 50)
    @Column(length = 50)
    private String beerName;

    @JdbcTypeCode(value = SqlTypes.TINYINT)
    private BeerStyle beerStyle;

    @Size(max = 255)
    private String upc;

    private Integer quantityOnHand;

    private BigDecimal price;

    private LocalDateTime createdDate;

    private LocalDateTime updateDate;

    // Estos campos son nuevos
    @CreationTimestamp
    private LocalDateTime createdDateAudit;

    private String principalName;

    private String auditEventType;
}