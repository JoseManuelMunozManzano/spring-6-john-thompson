package com.jmunoz.reactivemongo.bootstrap;

import com.jmunoz.reactivemongo.domain.Beer;
import com.jmunoz.reactivemongo.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Inicialización de data
// Gracias a que implementa CommandLineRunner, esta clase se ejecuta cuando comienza el contexto de Spring.
// También se ejecuta en los tests en los que se utiliza el contexto Spring.
@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;

    @Override
    public void run(String... args) throws Exception {
        // Si el delete va bien, se carga la data
        beerRepository.deleteAll()
                .doOnSuccess(success -> {
                    loadBeerData();
                })
                .subscribe();
    }

    private void loadBeerData() {
        // Igual que hicimos en la sección de Spring Reactivo
        beerRepository.count().subscribe(count -> {
            if (count == 0) {
                Beer beer1 = Beer.builder()
                        .beerName("Galaxy Cat")
                        .beerStyle("Pale Ale")
                        .upc("12356")
                        .price(new BigDecimal("12.99"))
                        .quantityOnHand(122)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Beer beer2 = Beer.builder()
                        .beerName("Crank")
                        .beerStyle("Pale Ale")
                        .upc("12356222")
                        .price(new BigDecimal("11.99"))
                        .quantityOnHand(392)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Beer beer3 = Beer.builder()
                        .beerName("Sunshine City")
                        .beerStyle("IPA")
                        .upc("12356")
                        .price(new BigDecimal("13.99"))
                        .quantityOnHand(144)
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

//                beerRepository.save(beer1).subscribe(beer -> {
//                    System.out.println(beer.toString());
//                });
//                beerRepository.save(beer2).subscribe(beer -> {
//                    System.out.println(beer.toString());
//                });
//                beerRepository.save(beer3).subscribe(beer -> {
//                    System.out.println(beer.toString());
//                });
//
//                System.out.println("Loaded Beers: " + beerRepository.count().block());

                beerRepository.save(beer1).subscribe();
                beerRepository.save(beer2).subscribe();
                beerRepository.save(beer3).subscribe();
            }
        });
    }
}
