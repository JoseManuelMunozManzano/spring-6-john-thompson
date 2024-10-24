package com.jmunoz.restmvc.repositories;

import com.jmunoz.restmvc.entities.BeerEntity;
import com.jmunoz.restmvc.entities.CategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BeerRepository beerRepository;
    BeerEntity testBeer;

    @BeforeEach
    void setUp() {
        testBeer = beerRepository.findAll().getFirst();
    }

    @Transactional
    @Test
    void testAddCategory() {
        // Recordar que al hacer save, debemos obtener el objeto salvado y trabajar con él.
        // Para establecer bien las relaciones es una buena práctica usar helper methods (cosa que hacemos)
        // En relaciones Many to Many es incluso más beneficioso tener estos helper methods.
        CategoryEntity savedCat = categoryRepository.save(CategoryEntity.builder()
                        .description("Ales")
                .build());

        testBeer.addCategory(savedCat);
        BeerEntity saveBeer = beerRepository.save(testBeer);

        // Aquí podemos poner un debug para ver si se ha establecido bien la relación y tenemos saveBeer.categories y savedCat.beers
        // Esta bien gracias al helper method.
        System.out.println(saveBeer.getBeerName());
    }
}