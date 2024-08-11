package com.jmunoz.restmvc.repositories;

import com.jmunoz.restmvc.entities.BeerEntity;
import com.jmunoz.restmvc.entities.CustomerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        // Con esto compondremos una BeerOrderEntity.
        testCustomer = customerRepository.findAll().getFirst();
        testBeer = beerRepository.findAll().getFirst();
    }

    @Test
    void testBeerOrders() {
        // Puede (@SopringBootTest) o no (@DataJpaTest) haber data en BD en función de la anotación de clase.
        System.out.println(beerOrderRepository.count());
        System.out.println(customerRepository.count());
        System.out.println(beerRepository.count());

        System.out.println(testCustomer.getName());
        System.out.println(testBeer.getBeerName());
    }
}