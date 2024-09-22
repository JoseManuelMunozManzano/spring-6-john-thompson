package com.jmunoz.reactivemongo.repositories;

import com.jmunoz.reactivemongo.domain.Beer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface BeerRepository extends ReactiveMongoRepository<Beer, String> {

    // Al igual que con Spring Data JPA, con Spring WebFlux también podemos montar query methods.
    // Como la DB no es Unique, tampoco el beerName, va a devolver la primera Beer entity,
    // cosa que nos viene bien.
    Mono<Beer> findFirstByBeerName(String beerName);
}
