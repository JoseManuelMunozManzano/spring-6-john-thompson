package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BeerClient {

    // Para poder trabajar con Page directamente sin usar Spring Data JPA, se incluye en el POM la dependencia
    // spring-data-commons
    // Indicar que en el proyecto spring-6-db-relationships, en el constructor BeerController, el méto-do listBeers
    // devuelve también Page<BeerDTO>. Mantenemos la consistencia.

    Page<BeerDTO> listBeers();

    // En este ejemplo pasamos parámetros.
    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory,
                            Integer pageNumber, Integer pageSize);

    BeerDTO getBeerById(UUID beerId);

    BeerDTO createBeer(BeerDTO beerDto);

    BeerDTO updateBeer(BeerDTO beerDto);

    void deleteBeer(UUID beerId);
}
