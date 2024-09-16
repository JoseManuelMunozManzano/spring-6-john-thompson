package com.jmunoz.reactivemongo.repositories;

import com.jmunoz.reactivemongo.domain.Beer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BeerRepository extends ReactiveMongoRepository<Beer, String> {
}
