package com.jmunoz.reactivemongo.repositories;

import com.jmunoz.reactivemongo.domain.Beer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CustomerRepository extends ReactiveMongoRepository<Beer, String> {
}
