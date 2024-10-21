package com.jmunoz.restmvc.repositories;

import com.jmunoz.restmvc.entities.CustomerEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

// Hibernate hará reflexión sobre las dos entidades creadas.
// Creará la BBDD H2 en memoria autoconfigurada por Spring Boot y la inicializa.
// Notar que solo se usa una mínima configuración, ya que los controllers y otros componentes de Spring
// no los incluimos en el contexto de Spring. Solo lo mínimo para probar componentes Spring Data JPA.
@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testSaveCustomer() {
        CustomerEntity customer = customerRepository.save(CustomerEntity.builder()
                        .name("New Name")
                .build());

        assertThat(customer.getId()).isNotNull();
    }
}