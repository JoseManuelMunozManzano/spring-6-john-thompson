package com.jmunoz.restmvc.repositories;

import com.jmunoz.restmvc.entities.BeerEntity;
import com.jmunoz.restmvc.entities.BeerOrderEntity;
import com.jmunoz.restmvc.entities.CustomerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

// @DataJpaTest: Podemos cargar este slice de SpringBootTest.
//          Recordar que no se carga ninguna data en la BD al traernos este test slice.
//          Pero, normalmente para testing, el repository usa esta anotación.
//
// @SpringBootTest: Si cargamos toda la suite de tests, sí que tenemos data que viene de la BD.
//          Esto es porque se ejecuta la capa de boostrap, que carga data en BD.
//          Como vamos a hacer tests complejos, utilizamos esta anotación para facilitarnos las cosas.
@SpringBootTest
class BeerOrderRepositoryTest {

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    // Cuando se quieren hacer tests complejos, es buena idea configurar la data
    // que queremos usar, usando properties y el método de configuración.
    CustomerEntity testCustomer;
    BeerEntity testBeer;

    @BeforeEach
    void setUp() {
        // Esta es la data que queremos usar para nuestros tests.
        // También podríamos crear data más concreta, pero vamos a usar el primer registro de nuestra BD.
        testCustomer = customerRepository.findAll().getFirst();
        testBeer = beerRepository.findAll().getFirst();
    }

    @Transactional
    @Test
    void testBeerOrders() {
        // Puede (@SopringBootTest) o no (@DataJpaTest) haber data en BD en función de la anotación de clase.
        //
        // System.out.println(beerOrderRepository.count());
        // System.out.println(customerRepository.count());
        // System.out.println(beerRepository.count());
        // System.out.println(testCustomer.getName());
        // System.out.println(testBeer.getBeerName());

        // Componemos una BeerOrderEntity
        BeerOrderEntity beerOrder = BeerOrderEntity.builder()
                .customerRef("Test order")
                .customer(testCustomer)
                .build();

        // Importante hacer el flush para que Hibernate persista inmediatamente la data a la BD.
        // Si no se indica, la data se queda en Hibernate, lo que hace que el test pueda fallar.
        //
        // Esto es también porque en un contexto transaccional se hace una carga perezosa (lazy load)
        // y estamos preguntando, dentro de customer, por beerOrders, que contendrá el elemento que hemos compuesto, y es lazy load.
        // De nuevo, al hacer el flush, se persiste to-do a BD y obtenemos la relación bidireccional entre ambas entidades.
        //
        // A partir de ahora, en el test, se debe usar el objeto devuelto por el repositorio porque no se garantiza
        // que al hacer el save() se devuelva la misma referencia.
        //
        // El problema de saveAndFlush() es que causa una degradación del rendimiento en operaciones complejas,
        // por lo que se vuelve a usar save() y se va a crear un helper method (un setter sobreescrito en este caso)
        // en BeerOrderEntity.
        //
        //BeerOrderEntity savedBeerOrder = beerOrderRepository.saveAndFlush(beerOrder);
        BeerOrderEntity savedBeerOrder = beerOrderRepository.save(beerOrder);

        // Aquí podemos poner un debug para ver si se ha establecido bien la relación
        System.out.println(savedBeerOrder.getCustomerRef());
    }
}