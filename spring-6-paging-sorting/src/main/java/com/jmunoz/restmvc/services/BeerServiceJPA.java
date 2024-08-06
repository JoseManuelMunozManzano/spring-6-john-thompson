package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.mappers.BeerMapper;
import com.jmunoz.restmvc.model.BeerDto;
import com.jmunoz.restmvc.model.BeerStyle;
import com.jmunoz.restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

// Como ahora vamos a tener dos implementaciones de BeerService, este que usa JPA lo hacemos @Primary
@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    // Usamos el repositorio en conjunción con el mapper.
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    // El número de página es 0-index, pero en nuestra API empieza por 1, así que eso hay que configurarlo
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    @Override
    public Page<BeerDto> listBeers(Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        // Está bien si no encuentra nada
        return beerRepository.findAll(pageRequest).map(beerMapper::beerEntityToBeerDto);
    }

    @Override
    public Page<BeerDto> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle, Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle, pageRequest)
                .map(beerMapper::beerEntityToBeerDto);
    }

    public Page<BeerDto> listBeersByNameContainingIgnoreCase(String beerName, Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageRequest)
                .map(beerMapper::beerEntityToBeerDto);
    }

    @Override
    public Page<BeerDto> listBeersByStyle(BeerStyle beerStyle, Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        return beerRepository.findAllByBeerStyle(beerStyle, pageRequest)
                .map(beerMapper::beerEntityToBeerDto);
    }

    // Este es nuestro método para configurar PageRequest.
    // Tiene un control para que el tamaño de registros requeridos nunca sea mayor de 1000.
    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }

        // Todavía no ordenamos, por eso no se usa los parámetros Direction
        return PageRequest.of(queryPageNumber, queryPageSize);
    }

    @Override
    public Optional<BeerDto> getBeerById(UUID id) {
        // Para una búsqueda concreta, si debemos indicar si no se ha encontrado
        // el elemento.
        return Optional.ofNullable(beerMapper.beerEntityToBeerDto(beerRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beer) {
        return beerMapper.beerEntityToBeerDto(beerRepository.save(beerMapper.beerDtoToBeerEntity(beer)));
    }

    // Las funciones lambda no actualizan ninguna variable local (foundBeer), ya que son effective final o final.
    // Cuando queremos actualizar un valor dentro de una función lambda, se usa AtomicReference porque es thread safe.
    @Override
    public Optional<BeerDto> updateBeerById(UUID beerId, BeerDto beer) {
        AtomicReference<Optional<BeerDto>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            foundBeer.setBeerName(beer.getBeerName());
            foundBeer.setBeerStyle(beer.getBeerStyle());
            foundBeer.setUpc(beer.getUpc());
            foundBeer.setPrice(beer.getPrice());
            foundBeer.setQuantityOnHand(beer.getQuantityOnHand());

            atomicReference.set(Optional.of(beerMapper
                    .beerEntityToBeerDto(beerRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    // En vez de devolver un Optional y manejar una excepción en el controller, en este caso utilizamos una bandera.
    // Si existe el id se devuelve true y si no se devuelve false.
    @Override
    public Boolean deleteBeerById(UUID beerId) {
        if (beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);
            return true;
        }

        return false;
    }

    @Override
    public Optional<BeerDto> patchBeerById(UUID beerId, BeerDto beer) {
        AtomicReference<Optional<BeerDto>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            if (StringUtils.hasText(beer.getBeerName())) {
                foundBeer.setBeerName(beer.getBeerName());
            }

            if (beer.getBeerStyle() != null) {
                foundBeer.setBeerStyle(beer.getBeerStyle());
            }

            if (beer.getPrice() != null) {
                foundBeer.setPrice(beer.getPrice());
            }

            if (beer.getQuantityOnHand() != null) {
                foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
            }

            if (StringUtils.hasText(beer.getUpc())) {
                foundBeer.setUpc(beer.getUpc());
            }

            atomicReference.set(Optional.of(beerMapper
                    .beerEntityToBeerDto(beerRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
