package com.jmunoz.restmvc.bootstrap;

import com.jmunoz.restmvc.repositories.BeerOrderRepository;
import com.jmunoz.restmvc.repositories.BeerRepository;
import com.jmunoz.restmvc.repositories.CustomerRepository;
import com.jmunoz.restmvc.services.BeerCsvService;
import com.jmunoz.restmvc.services.BeerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

// Se corrige este test, que usa el splice @DataJpaTest. El contexto de Spring no hace un escaneo de
// componentes completo, sino que se limita a componentes de datos en los repositorios (BeerCsvService no es un repositorio)
// Lo que hacemos es importar la clase directamente, usando la anotación @Import
@DataJpaTest
@Import(BeerCsvServiceImpl.class)
class BootstrapDataTest {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    BeerCsvService csvService;

    BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(beerRepository, customerRepository, beerOrderRepository, csvService);
    }

    @Test
    void run() throws Exception {
        bootstrapData.run(null);

        assertThat(beerRepository.count()).isEqualTo(2413);
        assertThat(customerRepository.count()).isEqualTo(3);
    }
}