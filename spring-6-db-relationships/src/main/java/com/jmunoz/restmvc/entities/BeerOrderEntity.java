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
// El @Builder no accede a los setter definidos explícitamente. Eliminamos @AllArgsConstructor y creamos nuestro
// constructor
@Getter
@Setter
@Entity
@NoArgsConstructor
// @AllArgsConstructor
@Builder
@Table(name="beer_order")
public class BeerOrderEntity {

    // Creamos nuestro constructor y
    //   - Usamos nuestro setter
    public BeerOrderEntity(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, String customerRef, CustomerEntity customer, Set<BeerOrderLineEntity> beerOrderLines, BeerOrderShipmentEntity beerOrderShipment) {
        this.id = id;
        this.version = version;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.customerRef = customerRef;
        this.setCustomer(customer);
        this.beerOrderLines = beerOrderLines;
        this.setBeerOrderShipment(beerOrderShipment);
    }

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

    // Ejemplo de relacion uno a uno
    // Vemos aquí el ejemplo de la operación Cascade.
    @OneToOne(cascade = CascadeType.PERSIST)
    private BeerOrderShipmentEntity beerOrderShipment;

    public boolean isNew() {
        return this.id == null;
    }

    // Helper method
    // La idea es, dado un Customer, guardarlo y añadirlo a beerOrders
    // Con esto evitamos el flush() en el test, porque ya lo hacemos aquí directamente.
    // El único problema que podemos tener es que el Set de beerOrders no esté inicializado, por lo que
    // lo inicializamos directamente en la clase CustomerEntity.
    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
        customer.getBeerOrders().add(this);
    }

    // Helper method
    public void setBeerOrderShipment(BeerOrderShipmentEntity beerOrderShipment) {
        this.beerOrderShipment = beerOrderShipment;
        beerOrderShipment.setBeerOrder(this);
    }
}
