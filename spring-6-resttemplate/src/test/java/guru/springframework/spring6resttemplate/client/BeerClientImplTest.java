package guru.springframework.spring6resttemplate.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClientImpl beerClient;

    // Esto no son tests como tal, más bien pruebas. Van a evolucionar.

    @Test
    void listBeersNoBeerName() {

        beerClient.listBeers(null);
    }

    @Test
    void listBeers() {

        beerClient.listBeers("ALE");
    }

}