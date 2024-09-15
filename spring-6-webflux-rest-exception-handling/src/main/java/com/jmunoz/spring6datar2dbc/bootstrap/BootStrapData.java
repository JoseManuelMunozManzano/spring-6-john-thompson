package com.jmunoz.spring6datar2dbc.bootstrap;

import com.jmunoz.spring6datar2dbc.domain.Beer;
import com.jmunoz.spring6datar2dbc.domain.Customer;
import com.jmunoz.spring6datar2dbc.repositories.BeerRepository;
import com.jmunoz.spring6datar2dbc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Inicializamos con data.
// Gracias a que implemente CommandLineRunner, Spring Boot lo ejecuta al iniciar la ejecución de la aplicación.
@RequiredArgsConstructor
@Component
public class BootStrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeerData();

        // Como es un Mono, usamos un subscriber
        beerRepository.count().subscribe(count -> {
            System.out.println("Beer count is: " + count);
        });

        loadCustomerData();

        // Como es un Mono, usamos un subscriber
        customerRepository.count().subscribe(count -> {
            System.out.println("Customer count is: " + count);
        });
    }

    private void loadBeerData() {

        // Como es un Mono, usamos un subscriber
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

                // Indicar que para persistir la data hay que subscribirse!
                // Así forzamos el back pressure.
                // Si no lo indicamos (el subscribe()) no se persiste nada.
                beerRepository.save(beer1).subscribe();
                beerRepository.save(beer2).subscribe();
                beerRepository.save(beer3).subscribe();
            }
        });
    }

    private void loadCustomerData() {
        // Como es un Mono, usamos un subscriber
        customerRepository.count().subscribe(count -> {
            if (count == 0) {
                Customer customer1 = Customer.builder()
                        .customerName("Jim")
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                Customer customer2 = Customer.builder()
                        .customerName("Laura")
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .build();

                // Indicar que para persistir la data hay que subscribirse!
                // Así forzamos el back pressure.
                // Si no lo indicamos (el subscribe()) no se persiste nada.
                customerRepository.save(customer1).subscribe();
                customerRepository.save(customer2).subscribe();

            }
        });
    }
}
