package com.jmunoz.restmvc.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

// No se recomienda @Data en Entities. Se cambia por @Setter y @Getter
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="beer_order")
public class BeerOrderEntity {

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

    private String customerRef;

    // Relación bidireccional con CustomerEntity
    @ManyToOne
    private CustomerEntity customer;

    // Relación con BeerOrderLineEntity
    @OneToMany(mappedBy = "beerOrder")
    private Set<BeerOrderLineEntity> beerOrderLines;

    public boolean isNew() {
        return this.id == null;
    }

}
