package com.jmunoz.restmvc.repositories;

import com.jmunoz.restmvc.entities.BeerEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Sobreescribimos algunas de las propiedades indicadas en application-localmysql.properties
// Para eso, primero lo cargamos
@Testcontainers
@SpringBootTest
@ActiveProfiles("localmysql")
public class MySqlTest {

    // Indicamos el nombre de la imagen del contenedor
    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:9");

    // Aquí es donde sobreescribimos algunas de las propiedades que no funcionan para el test container
    // ya que esta configuración es para mysql de producción.
    // Sustituimos dichas propiedades por las del testContainer.
    @DynamicPropertySource
    static void mySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    }

    // Realmente no nos hace falta, es solo para ver la data que tiene y ver los valores
    // sobreescritos por mySqlContainer
    @Autowired
    DataSource dataSource;

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testListBeers() {
        List<BeerEntity> beers = beerRepository.findAll();

        assertThat(beers.size()).isGreaterThan(0);
    }
}
