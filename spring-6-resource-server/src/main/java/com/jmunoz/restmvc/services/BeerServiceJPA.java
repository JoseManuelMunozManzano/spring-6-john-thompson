package com.jmunoz.restmvc.services;

import com.jmunoz.restmvc.mappers.BeerMapper;
import com.jmunoz.restmvc.model.BeerDto;
import com.jmunoz.restmvc.model.BeerStyle;
import com.jmunoz.restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

// Como ahora vamos a tener dos implementaciones de BeerService, este que usa JPA lo hacemos @Primary
@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    // Usamos el repositorio en conjunción con el mapper.
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    // Uso de Cache Manager para evitar el problema de juntar en una misma clase el cacheo Proxy AOP y
    // eviction proxy AOP.
    private final CacheManager cacheManager;

    // El número de página es 0-index, pero en nuestra API empieza por 1, así que eso hay que configurarlo
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    // Cacheamos, sin indicar esta vez las keys.
    // Spring crea la key, una combinación de todos los parámetros.
    // Cuando cambie la key, no se encontrará la caché y se ejecutará el méto-do de nuevo.
    @Cacheable(cacheNames = "beerListCache")
    @Override
    public Page<BeerDto> listBeers(Integer pageNumber, Integer pageSize) {

        log.info("List Beers - in service");

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        // Está bien si no encuentra nada
        return beerRepository.findAll(pageRequest).map(beerMapper::beerEntityToBeerDto);
    }

    // Cacheamos, sin indicar esta vez las keys.
    // Spring crea la key, una combinación de todos los parámetros.
    // Cuando cambie la key, no se encontrará la caché y se ejecutará el méto-do de nuevo.
    @Cacheable(cacheNames = "beerListCache")
    @Override
    public Page<BeerDto> listBeersByNameAndStyle(String beerName, BeerStyle beerStyle, Integer pageNumber, Integer pageSize) {

        log.info("List Beers - in service");

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        return beerRepository.findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle("%" + beerName + "%", beerStyle, pageRequest)
                .map(beerMapper::beerEntityToBeerDto);
    }

    // Cacheamos, sin indicar esta vez las keys.
    // Spring crea la key, una combinación de todos los parámetros.
    // Cuando cambie la key, no se encontrará la caché y se ejecutará el méto-do de nuevo.
    @Cacheable(cacheNames = "beerListCache")
    public Page<BeerDto> listBeersByNameContainingIgnoreCase(String beerName, Integer pageNumber, Integer pageSize) {

        log.info("List Beers - in service");

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageRequest)
                .map(beerMapper::beerEntityToBeerDto);
    }

    // Cacheamos, sin indicar esta vez las keys.
    // Spring crea la key, una combinación de todos los parámetros.
    // Cuando cambie la key, no se encontrará la caché y se ejecutará el méto-do de nuevo.
    @Cacheable(cacheNames = "beerListCache")
    @Override
    public Page<BeerDto> listBeersByStyle(BeerStyle beerStyle, Integer pageNumber, Integer pageSize) {

        log.info("List Beers - in service");

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

        // Añadimos ordenación. Puede ser muy complejo si lo necesitamos, pero aquí lo vamos a hacer muy sencillo.
        // El .asc es por defecto, pero siempre viene bien indicarlo, por si lo ve otro desarrollador sepa qué es.
        Sort sort = Sort.by(Sort.Order.asc("beerName"));

        // Todavía no ordenamos, por eso no se usa los parámetros Direction
        // return PageRequest.of(queryPageNumber, queryPageSize);
        //
        // Ordenando
        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    // En este méto-do vamos a habilitar cashing.
    // La key es opcional y en este caso ni haría falta. Usa Spring Expression Language.
    // En este caso solo tenemos un parámetro, e indica que se use el valor id.
    // Spring va a crear automáticamente las keys por nosotros, usando reflexión.
    //
    // Cómo funciona: Cuando llega la petición, Spring usa AOP para interceptar dicha request,
    // busca si ya está cacheado y devuelve el valor sin siquiera tener que invocar este méto-do.
    @Cacheable(cacheNames = "beerCache", key = "#id")
    @Override
    public Optional<BeerDto> getBeerById(UUID id) {

        log.info("Get Beer by Id - in service");

        // Para una búsqueda concreta, si debemos indicar si no se ha encontrado
        // el elemento.
        return Optional.ofNullable(beerMapper.beerEntityToBeerDto(beerRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beer) {

        // Limpiamos la caché
        if (cacheManager.getCache("beerListCache") != null) {
            cacheManager.getCache("beerListCache").clear();
        }

        return beerMapper.beerEntityToBeerDto(beerRepository.save(beerMapper.beerDtoToBeerEntity(beer)));
    }

    // Las funciones lambda no actualizan ninguna variable local (foundBeer), ya que son effective final o final.
    // Cuando queremos actualizar un valor dentro de una función lambda, se usa AtomicReference porque es thread safe.
    @Override
    public Optional<BeerDto> updateBeerById(UUID beerId, BeerDto beer) {

        clearCache(beerId);

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

    private void clearCache(UUID beerId) {

        // Sustituimos la anotación que hay en deleteBeerById() por esta programación.
        // Podemos usar el méto-do evict, donde indicamos la/s key/s a limpiar.
        // O podemos usar el méto-do clear, donde se limpiar to-do.
        if (cacheManager.getCache("beerCache") != null) {
            cacheManager.getCache("beerCache").evict(beerId);
        }

        if (cacheManager.getCache("beerListCache") != null) {
            cacheManager.getCache("beerListCache").clear();
        }
    }

    // En vez de devolver un Optional y manejar una excepción en el controller, en este caso utilizamos una bandera.
    // Si existe el id se devuelve true y si no se devuelve false.
    //
    // Para el tema de cache, cuando hagamos una operación de manipulación de data (save, update, patch, delete),
    // vamos a querer limpiarla.
    // Pero esta anotación por si sola no funciona.
    // Funcionaría si esta operación de delete estuviera en otra clase completamente distinta, pero el problema
    // es que está en la misma clase donde más arriba, tenemos @Cacheable. Esto provoca un problema porque estamos
    // cogiendo el cacheo proxy AOP, pero entonces NO cogemos el evict (limpieza de caché) proxy.
    // Una forma de evitarlo sería usando AspectJ, otra forma de trabajar con aspectos.
    // Otra forma de solucionarlo sería, como se ha dicho, usar otras clases, pero es demasiada refactorización.
    // Vamos a usar una solución alternativa, Spring Cache Manager.
    // Por lo tanto, esto lo comentamos porque ya no nos sirve.
//    @Caching(evict = {
//            @CacheEvict(cacheNames = "beerCache", key = "#beerId"),
//            @CacheEvict(cacheNames = "beerListCache")
//    })
    @Override
    public Boolean deleteBeerById(UUID beerId) {

        clearCache(beerId);

        if (beerRepository.existsById(beerId)) {
            beerRepository.deleteById(beerId);
            return true;
        }

        return false;
    }

    @Override
    public Optional<BeerDto> patchBeerById(UUID beerId, BeerDto beer) {

        clearCache(beerId);

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
