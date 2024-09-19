package com.jmunoz.reactivemongo.services;

import com.jmunoz.reactivemongo.domain.Beer;
import com.jmunoz.reactivemongo.mappers.BeerMapper;
import com.jmunoz.reactivemongo.mappers.BeerMapperImpl;
import com.jmunoz.reactivemongo.model.BeerDTO;
import com.jmunoz.reactivemongo.repositories.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;

// No hay mocking, así que son tests de integración reales que van a almacenar la data realmente en BD.
// En producción vamos a querer usar streams, es decir, subscribe(), no .block()
@SpringBootTest
class BeerServiceImplTest {

    @Autowired
    BeerService beerService;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    BeerRepository beerRepository;

    BeerDTO beerDTO;

    @BeforeEach
    void setUp() {
        beerDTO = beerMapper.beerToBeerDto(getTestBeer());
    }

    @Test
    @DisplayName("Test Save Beer Using Subscriber")
    void saveBeer() {

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<BeerDTO> atomicDto = new AtomicReference<>();

        Mono<BeerDTO> savedMono = beerService.saveBeer(Mono.just(beerDTO));

        // En principio, si se hacen aserciones dentro de un subscribe, si falla va a eliminarse y el test pasa.
        // Para evitar esto, se usa un AtomicBoolean y se espera a que termine el subscribe.
        savedMono.subscribe(savedDto -> {
            System.out.println(savedDto.getId());
            atomicBoolean.set(true);
            atomicDto.set(savedDto);
        });

        // Como el test termina antes de que se complete el subscribe (por tanto, JVM también), hay que añadir esto.
        // Es muy feo, un antipatrón, y vamos a ver otra forma de hacerlo, mucho más elegante.
        // NOTA: Con esta opción hay que añadir a la función `throws InterruptedException`
        //
        // Thread.sleep(1000l);
        //
        // Usamos una utilidad Java llamada Awaitility y una bandera para saber cuando se ha completado el subscribe.
        await().untilTrue(atomicBoolean);
        
        BeerDTO persistedDto = atomicDto.get();
        assertThat(persistedDto).isNotNull();
        assertThat(persistedDto.getId()).isNotNull();
    }

    // Hecho como ejemplo, pero en Producción no es bueno usar .block(), aunque algunos opinan que en tests está
    // justificado. Comparando con la forma de grabar usando subscribe(), vemos que ahorramos bastante código.
    @Test
    @DisplayName("Test Save Beer Using Block")
    void testSaveBeerUseBlock() {
        BeerDTO savedDto = beerService.saveBeer(Mono.just(getTestBeerDto())).block();
        assertThat(savedDto).isNotNull();
        assertThat(savedDto.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test Update Beer Using Block 1")
    void testUpdateBlocking() {
        final String newName = "New Beer Name";  // use final so cannot mutate
        BeerDTO savedBeerDto = getSavedBeerDto(); // aquí se usa block
        savedBeerDto.setBeerName(newName);

        BeerDTO updatedBeerDto = beerService.updateBeer( savedBeerDto.getId(), savedBeerDto).block();

        assertThat(updatedBeerDto).isNotNull();
        assertThat(updatedBeerDto.getBeerName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("Test Update Using Reactive Streams")
    void testUpdateStreaming() {
        final String newName = "New Beer Name";  // use final so cannot mutate

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<BeerDTO> atomicDto = new AtomicReference<>();

        beerService.saveBeer(Mono.just(getTestBeerDto()))
                .subscribe(savedDTO -> atomicDto.set(savedDTO));

        await().until(() -> atomicDto.get() != null);

        atomicDto.get().setBeerName(newName);

        beerService.updateBeer(atomicDto.get().getId(), atomicDto.get())
                .subscribe( beerDto -> {
                    assertTrue(beerDto.getBeerName().equals(newName));
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }

    @Test
    @DisplayName("Test Patch Using Reactive Streams")
    void testPatch() {
        final String newName = "New Beer Name";  // use final so cannot mutate

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        BeerDTO savedBeer = getSavedBeerDto();
        savedBeer.setBeerName(newName);

        Mono<BeerDTO> beerDTOMono = beerService.patchBeer(savedBeer.getId(), savedBeer);

        beerDTOMono.subscribe( beerDto -> {
            assertTrue( beerDto.getBeerName().equals(newName));
            atomicBoolean.set(true);
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testDeleteBeer() {
        BeerDTO beerToDelete = getSavedBeerDto();

        beerService.deleteBeerById(beerToDelete.getId()).block();

        Mono<BeerDTO> expectedEmptyBeerMono = beerService.getById(beerToDelete.getId());

        BeerDTO emptyBeer = expectedEmptyBeerMono.block();

        assertThat(emptyBeer).isNull();

    }

    public BeerDTO getSavedBeerDto() {
        return beerService.saveBeer(Mono.just(getTestBeerDto())).block();
    }

    public static BeerDTO getTestBeerDto() {
        return new BeerMapperImpl().beerToBeerDto(getTestBeer());
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