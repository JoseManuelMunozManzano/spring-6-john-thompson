package com.jmunoz.restmvc.bootstrap;

import com.jmunoz.restmvc.entities.BeerEntity;
import com.jmunoz.restmvc.entities.BeerOrderEntity;
import com.jmunoz.restmvc.entities.BeerOrderLineEntity;
import com.jmunoz.restmvc.entities.CustomerEntity;
import com.jmunoz.restmvc.model.BeerCSVRecord;
import com.jmunoz.restmvc.repositories.BeerOrderRepository;
import com.jmunoz.restmvc.repositories.BeerRepository;
import com.jmunoz.restmvc.repositories.CustomerRepository;
import com.jmunoz.restmvc.services.BeerCsvService;
import guru.springframework.spring6restmvcapi.model.BeerStyle;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerCsvService beerCsvService;

    // Con @Transactional, o se persiste to-do, o nada
    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCsvData();
        loadCustomerData();
        loadOrderData();
    }

    private void loadCsvData() throws FileNotFoundException {
        if (beerRepository.count() < 10) {
            File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

            List<BeerCSVRecord> recs = beerCsvService.convertCSV(file);

            // Cada registro lo guardo en BD
            recs.forEach(beerCSVRecord -> {
                // Mapeando de texto del CSV a mi enumeración
                BeerStyle beerStyle = switch (beerCSVRecord.getStyle()) {
                    case "American Pale Lager" -> BeerStyle.LAGER;
                    case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                            BeerStyle.ALE;
                    case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
                    case "American Porter" -> BeerStyle.PORTER;
                    case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
                    case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
                    case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
                    case "English Pale Ale" -> BeerStyle.PALE_ALE;
                    default -> BeerStyle.PILSNER;
                };

                beerRepository.save(BeerEntity.builder()
                                .beerName(StringUtils.abbreviate(beerCSVRecord.getBeer(), 50))
                                .beerStyle(beerStyle)
                                .price(BigDecimal.TEN)
                                .upc(beerCSVRecord.getRow().toString())
                                .quantityOnHand(beerCSVRecord.getCount())
                        .build());
            });
        }
    }

    private void loadBeerData() {

        if (beerRepository.count() == 0) {
            BeerEntity beer1 = BeerEntity.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("123456")
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(122)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            BeerEntity beer2 = BeerEntity.builder()
                    .beerName("Crank")
                    .beerStyle(BeerStyle.IPA)
                    .upc("1235622")
                    .price(new BigDecimal("12.99"))
                    .quantityOnHand(392)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            BeerEntity beer3 = BeerEntity.builder()
                    .beerName("Sunshine City")
                    .beerStyle(BeerStyle.IPA)
                    .upc("12356")
                    .price(new BigDecimal("13.99"))
                    .quantityOnHand(144)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            beerRepository.save(beer1);
            beerRepository.save(beer2);
            beerRepository.save(beer3);
        }
    }

    private void loadCustomerData() {

        if (customerRepository.count() == 0) {
            CustomerEntity jm = CustomerEntity.builder()
                    .name("José Manuel")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            CustomerEntity adri = CustomerEntity.builder()
                    .name("Adriana")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            CustomerEntity marina = CustomerEntity.builder()
                    .name("Marina")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            customerRepository.saveAll(Arrays.asList(jm, adri, marina));
        }
    }

    private void loadOrderData() {

        if (beerOrderRepository.count() == 0) {

            val customers = customerRepository.findAll();
            val beers = beerRepository.findAll();

            val beerIterator = beers.iterator();

            customers.forEach(customer -> {

                beerOrderRepository.save(BeerOrderEntity.builder()
                                .customer(customer)
                                .beerOrderLines(Set.of(
                                        BeerOrderLineEntity.builder()
                                                .beer(beerIterator.next())
                                                .orderQuantity(1)
                                                .build(),
                                        BeerOrderLineEntity.builder()
                                                .beer(beerIterator.next())
                                                .orderQuantity(2)
                                                .build()
                                )).build());

                beerOrderRepository.save(BeerOrderEntity.builder()
                        .customer(customer)
                        .beerOrderLines(Set.of(
                                BeerOrderLineEntity.builder()
                                        .beer(beerIterator.next())
                                        .orderQuantity(1)
                                        .build(),
                                BeerOrderLineEntity.builder()
                                        .beer(beerIterator.next())
                                        .orderQuantity(2)
                                        .build()
                        )).build());
            });

            // Poner aquí un debug para ver si se añaden las líneas de las órdenes a las beerOrders y ejecutar esta línea
            val orders = beerOrderRepository.findAll();
        }
    }

}
