package com.jmunoz.spring6datar2dbc.repositories;

import com.jmunoz.spring6datar2dbc.domain.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

// Notar el uso de ReactiveCrudRepository
// Obtenemos tipos reactivos usando este repository, que abstrae la naturaleza reactiva del driver reactivo de las distintas bases de datos.
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
}
