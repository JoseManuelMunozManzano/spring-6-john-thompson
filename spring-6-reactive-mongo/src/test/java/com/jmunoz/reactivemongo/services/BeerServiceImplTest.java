package com.jmunoz.reactivemongo.services;

import com.jmunoz.reactivemongo.domain.Beer;
import com.jmunoz.reactivemongo.mappers.BeerMapper;
import com.jmunoz.reactivemongo.model.BeerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;

@SpringBootTest
class BeerServiceImplTest {

    @Autowired
    BeerService beerService;

    @Autowired
    BeerMapper beerMapper;

    BeerDTO beerDTO;

    @BeforeEach
    void setUp() {
        beerDTO = beerMapper.beerToBeerDto(getTestBeer());
    }

    @Test
    void saveBeer() {

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        Mono<BeerDTO> savedMono = beerService.saveBeer(beerDTO);

        savedMono.subscribe(savedDto -> {
            System.out.println(savedDto.getId());
            atomicBoolean.set(true);
        });

        // Como el test termina antes de que se complete el subscribe (por tanto, JVM también), hay que añadir esto.
        // Es muy feo, un antipatrón, y vamos a ver otra forma de hacerlo, mucho más elegante.
        // NOTA: Con esta opción hay que añadir a la función `throws InterruptedException`
        //
        // Thread.sleep(1000l);
        //
        // Usamos una utilidad Java llamada Awaitility y una bandera para saber cuando se ha completado el subscribe.
        await().untilTrue(atomicBoolean);

    }

    public static Beer getTestBeer() {
        return Beer.builder()
                .beerName("Space Dust")
                .beerStyle("IPA")
                .price(BigDecimal.TEN)
                .quantityOnHand(12)
                .upc("123213")
                .build();
    }
}