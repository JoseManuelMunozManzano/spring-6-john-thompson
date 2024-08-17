package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClientImpl beerClient;

    // Esto no son tests como tal, más bien pruebas. Van a evolucionar.

    @Test
    void listBeersNoParams() {

        beerClient.listBeers();
    }

    @Test
    void listBeers() {

        beerClient.listBeers("ALE", BeerStyle.ALE, true, null, null);
    }

    @Test
    void getBeerById() {

        Page<BeerDTO> beerDTOS = beerClient.listBeers();

        BeerDTO dto = beerDTOS.getContent().getFirst();

        BeerDTO byId = beerClient.getBeerById(dto.getId());

        assertNotNull(byId);
    }

    @Test
    void testCreateBeer() {

        BeerDTO newDto = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123245")
                .build();

        BeerDTO savedDto = beerClient.createBeer(newDto);

        assertNotNull(savedDto);
    }

    // El PUT toma una path parameter para el id y el body con la data.
    @Test
    void testUpdateBeer() {

        BeerDTO newDto = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs 2")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123245")
                .build();

        BeerDTO beerDto = beerClient.createBeer(newDto);

        final String newName = "Mango Bobs 3";
        beerDto.setBeerName(newName);
        BeerDTO updatedBeer = beerClient.updateBeer(beerDto);

        assertEquals(updatedBeer.getBeerName(), newName);
    }

    // El DELETE toma una path parameter para el id.
    // Pero primero creamos un registro nuevo que usamos para borrarlo.
    @Test
    void testDeleteBeer() {

        BeerDTO newDto = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs 2")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("123245")
                .build();

        BeerDTO beerDto = beerClient.createBeer(newDto);

        // Vemos que no esperamos nada de vuelta
        beerClient.deleteBeer(beerDto.getId());

        // La idea de este test es que al intentar acceder a ese objeto de nuevo,
        // como se borró y ya no existe, dará error 404, que es HttpClientErrorException
        assertThrows(HttpClientErrorException.class, () -> {
            beerClient.getBeerById(beerDto.getId());
        });
    }
}