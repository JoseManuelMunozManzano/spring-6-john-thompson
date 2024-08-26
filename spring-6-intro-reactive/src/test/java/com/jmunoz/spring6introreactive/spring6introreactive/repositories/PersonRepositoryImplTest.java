package com.jmunoz.spring6introreactive.spring6introreactive.repositories;

import com.jmunoz.spring6introreactive.spring6introreactive.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    // Gestión de errores usando programación reactiva
    @Test
    void testFindPersonByIdNotFound() {
        Flux<Person> personFlux = personRepository.findAll();

        // IMPORTANTE: Al tratar con Monos, todas las variables que se usen dentro del stream deben ser final.
        // Es decir, no se permite la mutación de variables en el stream.
        final Integer id = 8;

        // El id 8 no existe en la data que tenemos y no puede devolver nada.
        // El méto-do next() no devuelve nada, porque si se llama a next() teniendo un Flux vacío, devuelve un Mono vacío.
        // Pero es preferible ver un error.
        //
        // Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).next()
        //                .doOnError(throwable -> {
        //   System.out.println("Error occurred in flux");
        //    System.out.println(throwable.toString());
        // });
        //
        // Si cambiamos next() por single() si que obtenemos la excepción NoSuchElementException si no devuelve nada,
        // o IndexOutOfBoundsException si devuelve más de un elemento.
        // Es decir, espera sí o sí un elemento.
        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).single()
                        .doOnError(throwable -> {
                            System.out.println("Error occurred in flux");
                            System.out.println(throwable.toString());
                        });

        // Pero recordar que, para que se lance la excepción, debe existir un subscriber que ejerza el back pressure
        // para que se ejecute el código anterior.
        // En caso contrario no se lanza ninguna excepción.
        personMono.subscribe(person -> {
            System.out.println(person);
        }, throwable -> {
            System.out.println("Error occurred in mono");
            System.out.println(throwable.toString());
        });
    }

    // Tests sobre GetById
    // Para estos tests, lo mejor es usar block()
    @Test
    void testGetByIdNotFound() {
        Mono<Person> personMono = personRepository.getById(10);

        assertFalse(personMono.hasElement().block());

        Person person = personMono.block();

        assertThat(person).isNull();
    }

    @Test
    void testGetByIdFound() {
        Mono<Person> personMono = personRepository.getById(1);

        assertTrue(personMono.hasElement().block());

        Person person = personMono.block();

        assertThat(person).isNotNull();
        assertThat(person.getFirstName()).isEqualTo("Michael");
    }
}