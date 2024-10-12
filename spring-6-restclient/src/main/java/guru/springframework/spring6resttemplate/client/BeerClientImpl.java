package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BeerClientImpl implements BeerClient {

    public static final String GET_BEER_PATH = "/api/v1/beer";
    public static final String GET_BEER_BY_ID_PATH = "/api/v1/beer/{beerId}";

    private final RestClient.Builder restClientBuilder;

    @Override
    public Page<BeerDTO> listBeers() {
        return listBeers(null, null, null, null, null);
    }

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {

        RestClient restClient = restClientBuilder.build();

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(GET_BEER_PATH);

        if (beerName != null) {
            uriComponentsBuilder.queryParam("beerName", beerName);
        }

        if (beerStyle != null) {
            uriComponentsBuilder.queryParam("beerStyle", beerStyle);
        }

        if (showInventory != null) {
            uriComponentsBuilder.queryParam("showInventory", showInventory);
        }

        if (pageNumber != null) {
            uriComponentsBuilder.queryParam("pageNumber", pageNumber);
        }

        if (pageSize != null) {
            uriComponentsBuilder.queryParam("pageSize", pageSize);
        }

        // No debemos temer este warning porque estamos enlazados a la implementación de BeerDTOPageImpl
        return restClient.get()
                .uri(uriComponentsBuilder.toUriString())
                .retrieve()
                .body(BeerDTOPageImpl.class);
    }

    @Override
    public BeerDTO getBeerById(UUID beerId) {

        RestClient restClient = restClientBuilder.build();

        return restClient.get()
                .uri(uriBuilder -> uriBuilder.path(GET_BEER_BY_ID_PATH).build(beerId))
                .retrieve()
                .body(BeerDTO.class);
    }

    @Override
    public BeerDTO createBeer(BeerDTO beerDto) {

        // Creamos el RestClient
        RestClient restClient = restClientBuilder.build();

        // Solo se devuelve la location
        var location = restClient.post()
                .uri(uriBuilder -> uriBuilder.path(GET_BEER_PATH).build())
                .body(beerDto)
                .retrieve()
                .toBodilessEntity()
                .getHeaders()
                .getLocation();

        // Como location podría ser null, da un warning en getPath()
        // Habría que colocar algún tipo de defensa.
        // Aquí devolvemos una instancia de BeerDto (usando un get)
        return restClient.get()
                .uri(location.getPath())
                .retrieve()
                .body(BeerDTO.class);
    }

    @Override
    public BeerDTO updateBeer(BeerDTO beerDto) {

        RestClient restClient = restClientBuilder.build();

        restClient.put()
                .uri(uriBuilder -> uriBuilder.path(GET_BEER_BY_ID_PATH).build(beerDto.getId()))
                .body(beerDto)
                .retrieve()
                .toBodilessEntity();

        return getBeerById(beerDto.getId());

    }

    @Override
    public void deleteBeer(UUID beerId) {

        RestClient restClient = restClientBuilder.build();

        restClient.delete()
                .uri(uriBuilder -> uriBuilder.path(GET_BEER_BY_ID_PATH).build(beerId))
                .retrieve()
                // Esta parte hace falta para cuando no encuentra el id (test notfound)
                .toBodilessEntity();
    }
}
