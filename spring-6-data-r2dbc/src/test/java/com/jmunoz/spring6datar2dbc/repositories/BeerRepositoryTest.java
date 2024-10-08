package com.jmunoz.spring6datar2dbc.repositories;

import com.jmunoz.spring6datar2dbc.config.DatabaseConfig;
import com.jmunoz.spring6datar2dbc.domain.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

// Queremos probar una situación donde vamos a persistir data en la BD y verificar que ha funcionado.
// La anotación @DataR2dbcTest trae una configuración Spring mínima.
//
// Para coger la habilitación de los campos de auditoría, hay que importar explícitamente la configuración de BD.
@DataR2dbcTest
@Import(DatabaseConfig.class)
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void saveNewBeer() {
        // El méto-do save devuelve un Mono del objeto Beer.
        beerRepository.save(getTestBeer())
                .subscribe(beer -> {
                    System.out.println(beer);
                });
    }

    // Helper method para crear una objeto Beer
    Beer getTestBeer() {
        return Beer.builder()
                .beerName("Space Dust")
                .beerStyle("IPA")
                .price(BigDecimal.TEN)
                .quantityOnHand(12)
                .upc("123213")
                .build();
    }
}