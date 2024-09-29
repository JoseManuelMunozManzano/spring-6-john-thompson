package com.jmunoz.spring6webclient.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClient client;

    @Test
    void listBeer() {

        client.listBeer().subscribe(response -> {
            System.out.println(response);
        });

        // De nuevo, para evitar que el test termine antes de que termine el subscriber, indicamos
        // de forma fea, que espere. Esto lo cambiaremos luego por awaitility.
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}