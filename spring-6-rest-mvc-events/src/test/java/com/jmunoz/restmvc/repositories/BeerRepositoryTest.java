package com.jmunoz.restmvc.repositories;

import com.jmunoz.restmvc.bootstrap.BootstrapData;
import com.jmunoz.restmvc.entities.BeerEntity;
import com.jmunoz.restmvc.model.BeerStyle;
import com.jmunoz.restmvc.services.BeerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Hibernate hará reflexión sobre las dos entidades creadas.
// Creará la BBDD H2 en memoria autoconfigurada por Spring Boot y la inicializa.
// Notar que solo se usa una mínima configuración, ya que los controllers y otros componentes de Spring
// no los incluimos en el contexto de Spring. Solo lo mínimo para probar componentes Spring Data JPA.
//
// Al añadir JPA Validation a BeerEntity, vemos que sigue funcionando testSaveBeer, aunque solo se
// indica beerName (faltan los otros campos) Esto es porque la sesión termina muy rápidamente y no da tiempo
// a que Hibernate haga el flush() a la BBDD, ya que Hibernate hace una escritura lazy.
//
// Importamos la clase Bootstrap para informar data en la BD H2. Sin esto no tenemos datos, ya que @DataJpaTest
// es un test slice, no carga to-do el contexto de Spring, y no tenemos la carga de datos. También importamos
// la implementación del CSV para dicha carga de datos.
@DataJpaTest
@Import({BootstrapData.class, BeerCsvServiceImpl.class})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        // Para ver errores, solo hay que comentar alguno de los sets de datos de savedBeer.
        BeerEntity savedBeer = beerRepository.save(BeerEntity.builder()
                        .beerName("My Beer")
                        .beerStyle(BeerStyle.PALE_ALE)
                        .upc("33412734129")
                        .price(new BigDecimal("11.99"))
                .build());

        // Para ver los errores de JPA Validation hay que indicar explícitamente el método flush() del repository.
        // Esto indica a Hibernate que escriba inmediatamente a la BBDD.
        beerRepository.flush();

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    // JPA Validation
    // La longitud máxima de beerName es 50.
    // La excepción que se obtiene es DataIntegrityViolationException, pero usando la anotación @Size se obtiene
    // un error de validación Jakarta, otra excepción que es preferible.
    // En concreto obtenemos ConstraintViolationException
    @Test
    void testSaveBeerNameTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            BeerEntity savedBeer = beerRepository.save(BeerEntity.builder()
                    .beerName("My Beer Name With More Than Fifty String Characters Long")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("33412734129")
                    .price(new BigDecimal("11.99"))
                    .build());

            beerRepository.flush();
        });
    }

    // Testing Query Methods
    // Gracias al @Import, tenemos data en la BD H2
    @Test
    void testBetBeerListByName() {
        Page<BeerEntity> list = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%", null);

        assertThat(list.getContent().size()).isEqualTo(336);
    }
}