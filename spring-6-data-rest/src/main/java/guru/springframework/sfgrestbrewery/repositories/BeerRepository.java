package guru.springframework.sfgrestbrewery.repositories;


import guru.springframework.sfgrestbrewery.domain.Beer;
import guru.springframework.sfgrestbrewery.domain.BeerStyleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.UUID;

// Se puede personalizar el Path URL.
// Por defecto, Spring Data REST está mapeando la entidad beer al endpoint beers (plural).
// Pero si nuestra especificación API indica que debe ser en singular, es decir, endpoint beer, se puede cambiar usando
// la anotación @RepositoryRestResource()
// Esta anotación tiene muchas más posibilidades, no solo cambiar el path. Se puede indicar que no se exponga nada sobre
// la entidad, usando exported = false...
//
// Por motivos de demostración, indicar que también se puede hacer que la respuesta, en vez de mostrar
// beers [{...}] lo muestre también en singular o como queramos, aunque en este caso no tiene mucho sentido, al ser
// un array. Se hace con el atributo collectionResourceRel
//
// NOTA: En el atributo path no hay que poner /
@RepositoryRestResource(path = "beer", collectionResourceRel = "beer")
public interface BeerRepository extends JpaRepository<Beer, UUID> {
    Page<Beer> findAllByBeerName(String beerName, Pageable pageable);

    Page<Beer> findAllByBeerStyle(BeerStyleEnum beerStyle, Pageable pageable);

    Page<Beer> findAllByBeerNameAndBeerStyle(String beerName, BeerStyleEnum beerStyle, Pageable pageable);

    Beer findByUpc(String upc);
}
