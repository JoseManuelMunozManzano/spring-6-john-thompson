package com.jmunoz.restmvc.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.UUID;

// No se recomienda @Data en Entities. Se cambia por @Setter y @Getter
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="beer_order_line")
public class BeerOrderLineEntity {

    @Id
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    // Empieza en 0 y con cada actualización se incrementa en 1.
    @Version
    private Long version;

    // Usamos @Column(updatable = false) para no actualizar el campo de forma automática
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdDate;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;

    @Min(value = 1, message = "Quantity Allocated must be greater than 0")
    private Integer orderQuantity = 1;
    private Integer quantityAllocated = 0;

    // Relación bidireccional con BeerOrderEntity
    @ManyToOne
    private BeerOrderEntity beerOrder;

    // Relación bidireccional con BeerEntity
    @ManyToOne
    private BeerEntity beer;

    public boolean isNew() {
        return this.id == null;
    }
}
