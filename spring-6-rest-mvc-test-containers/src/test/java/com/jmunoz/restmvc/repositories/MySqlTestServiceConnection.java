package com.jmunoz.restmvc.repositories;

import com.jmunoz.restmvc.entities.BeerEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Este test es básicamente el mismo de MySqlTest, pero sobreescribiendo automáticamente
// las propiedades de MySql con las del test container, usando Service Connection.
@Testcontainers
@SpringBootTest
@ActiveProfiles("localmysql")
public class MySqlTestServiceConnection {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:9");

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testListBeers() {
        List<BeerEntity> beers = beerRepository.findAll();

        assertThat(beers.size()).isGreaterThan(0);
    }
}
