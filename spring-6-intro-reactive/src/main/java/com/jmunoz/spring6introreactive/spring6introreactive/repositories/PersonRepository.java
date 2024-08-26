package com.jmunoz.spring6introreactive.spring6introreactive.repositories;

import com.jmunoz.spring6introreactive.spring6introreactive.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonRepository {

    // Es Mono porque puede devolver 0 o 1
    Mono<Person> getById(Integer id);

    // Es Flux porque puede devolver 0 o muchos
    Flux<Person> findAll();
}
