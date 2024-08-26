package com.jmunoz.spring6introreactive.spring6introreactive.repositories;

import com.jmunoz.spring6introreactive.spring6introreactive.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

// En estos tests no se inyecta nada de Spring.
class PersonRepositoryImplTest {

    PersonRepository personRepository = new PersonRepositoryImpl();

    // Como hacer un bloqueo (cosa que NO hay que hacer) usando el méto-do block()
    // No es la forma preferida porque es una operación bloqueante.
    // Lo que decimos es: quiero la persona con el id 1 y voy a esperar hasta que me lo des.
    @Test
    void testMonoByIdBlock() {
        Mono<Person> personMono = personRepository.getById(1);

        Person person = personMono.block();

        System.out.println(person);
    }

    // Forma preferida de trabajar con Mono.
    // Lo que queremos es proveer un subscriber.
    // De esta forma NO bloqueamos, la operación se realiza de forma asíncrona.
    @Test
    void testGetByIdSubscriber() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.subscribe(person -> {
            System.out.println(person);
        });
    }

    // Se puede hacer un stream de funciones.
    // En este caso hacemos una operación map, en la que el stream que fluye de Mono (cero o un elemento) se transforma,
    // de un objeto Person a un String.
    @Test
    void testMapOperation() {
        Mono<Person> personMono = personRepository.getById(1);

        // Siempre hay que añadir un subscriber, que es el que pregunta por la info que fluye de Mono (o del map).
        // Sin un subscriber no se hace nada!!!
        personMono.map(person -> person.getFirstName())
                .subscribe(firstName -> System.out.println(firstName.toUpperCase()));

    }
}