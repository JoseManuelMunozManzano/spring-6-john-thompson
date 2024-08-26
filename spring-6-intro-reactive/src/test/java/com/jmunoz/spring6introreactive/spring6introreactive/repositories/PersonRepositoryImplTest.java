package com.jmunoz.spring6introreactive.spring6introreactive.repositories;

import com.jmunoz.spring6introreactive.spring6introreactive.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// En estos tests no se inyecta nada de Spring.
class PersonRepositoryImplTest {

    PersonRepository personRepository = new PersonRepositoryImpl();

    // Operaciones Mono
    //
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

    // Operaciones Flux
    //
    // Empezamos con la forma no recomendada de trabajar con Flux.
    // Lo que hace es bloquear y esperar a que venga el primer elemento, sin preocuparse del resto.
    // Solo obtenemos, por tanto, el primer elemento. Los demás no llegan a emitirse.
    @Test
    void testFluxBlockFirst() {
        Flux<Person> personFlux = personRepository.findAll();

        Person person = personFlux.blockFirst();

        System.out.println(person);
    }

    // Forma preferida de trabajar con Flux.
    // Lo que queremos es proveer un subscriber que se va a ejecutar por cada elemento en el Flux.
    // De esta forma NO bloqueamos, la operación se realiza de forma asíncrona y obtenemos todos los elementos.
    @Test
    void testFluxSubscriber() {
        Flux<Person> personFlux = personRepository.findAll();

        personFlux.subscribe(person -> {
            System.out.println(person);
        });
    }

    // Se puede hacer un stream de funciones.
    // En este caso hacemos una operación map, en la que el stream que fluye de Flux (cero o muchos elementos)
    // se transforma, de un objeto Person a un String.
    @Test
    void testFluxMap() {
        Flux<Person> personFlux = personRepository.findAll();

        personFlux.map(Person::getFirstName).subscribe(firstName -> {
            System.out.println(firstName.toUpperCase());
        });
    }

    // Pasando de Flux a List
    // En este caso nos apoyamos en Mono para obtener una lista a partir de cada uno de los elementos de Flux.
    // Notar que solo habrá un elemento (una lista)
    @Test
    void testFluxToList() {
        Flux<Person> personFlux = personRepository.findAll();

        Mono<List<Person>> listMono = personFlux.collectList();

        listMono.subscribe(list -> {
           list.forEach(person -> System.out.println(person.getFirstName()));
        });
    }

    // Filtros
    @Test
    void testFilterOnName() {
        personRepository.findAll()
                .filter(person -> person.getFirstName().equals("Fiona"))
                .subscribe(person -> System.out.println(person.getFirstName()));
    }

    // Este ejemplo es similar al anterior.
    // Notar que findAll() devuelve un Flux (cero o muchos), pero con .next() hacemos que devuelva un Mono (cero o uno)
    // Esto se hace para tener la flexibilidad de poder hacer operaciones adicionales sobre el Mono.
    @Test
    void testGetById() {
        Mono<Person> fionaMono = personRepository.findAll().filter(person -> person.getFirstName().equals("Fiona"))
                .next();

        fionaMono.subscribe(person -> System.out.println(person.getFirstName()));
    }
}