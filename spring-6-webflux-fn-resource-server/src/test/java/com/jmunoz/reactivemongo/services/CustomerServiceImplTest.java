package com.jmunoz.reactivemongo.services;

import com.jmunoz.reactivemongo.domain.Customer;
import com.jmunoz.reactivemongo.mappers.CustomerMapper;
import com.jmunoz.reactivemongo.mappers.CustomerMapperImpl;
import com.jmunoz.reactivemongo.model.CustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

// No hay mocking, así que son tests de integración reales que van a almacenar la data realmente en BD.
// En producción vamos a querer usar streams, es decir, subscribe(), no .block()
@SpringBootTest
public class CustomerServiceImplTest {

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerMapper customerMapper;

    CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customerDTO = customerMapper.customerToCustomerDto(getTestCustomer());
    }

    @Test
    @DisplayName("Test Save Customer Using Subscriber")
    void testSaveCustomer() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<CustomerDTO> atomicDto = new AtomicReference<>();

        Mono<CustomerDTO> savedMono = customerService.saveCustomer(customerDTO);

        // En principio, si se hacen aserciones dentro de un subscribe, si falla va a eliminarse y el test pasa.
        // Para evitar esto, se usa un AtomicBoolean y se espera a que termine el subscribe.
        savedMono.subscribe(savedDto -> {
            System.out.println(savedDto.getId());
            atomicBoolean.set(true);
            atomicDto.set(savedDto);
        });

        // Usamos una utilidad Java llamada Awaitility y una bandera para saber cuando se ha completado el subscribe.
        await().untilTrue(atomicBoolean);

        CustomerDTO persistedDto = atomicDto.get();
        assertThat(persistedDto).isNotNull();
        assertThat(persistedDto.getId()).isNotNull();
    }

    @Test
    @DisplayName("Test Update Customer Using Subscriber")
    void testUpdateCustomer() {
        // use final so cannot mutate
        final String newName = "New Customer Name";

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<CustomerDTO> atomicDto = new AtomicReference<>();

        customerService.saveCustomer(getTestCustomerDto())
                .subscribe(atomicDto::set);

        await().until(() -> atomicDto.get() != null);

        atomicDto.get().setCustomerName(newName);

        customerService.updateCustomer(atomicDto.get().getId(), atomicDto.get())
                .subscribe(customerDto -> {
                    assertEquals(newName, customerDto.getCustomerName());
                    atomicBoolean.set(true);
                });

        await().untilTrue(atomicBoolean);
    }

    @Test
    @DisplayName("Test Patch Customer Using Subscriber")
    void testPatchCustomer() {
        // use final so cannot mutate
        final String newName = "Leo";

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        CustomerDTO savedCustomer = getSavedCustomerDto();
        savedCustomer.setCustomerName(newName);

        Mono<CustomerDTO> customerDTOMono = customerService.pathCustomer(savedCustomer.getId(), savedCustomer);

        customerDTOMono.subscribe(customerDto -> {
            assertEquals(newName, customerDto.getCustomerName());
            atomicBoolean.set(true);
        });

        await().untilTrue(atomicBoolean);
    }

    @Test
    @DisplayName("Test Delete Customer Using Block")
    void testDeleteCustomer() {
        CustomerDTO customerToDelete = getSavedCustomerDto();

        customerService.deleteCustomerById(customerToDelete.getId()).block();

        Mono<CustomerDTO> expectedEmptyCustomerMono = customerService.getById(customerToDelete.getId());

        CustomerDTO emptyCustomer = expectedEmptyCustomerMono.block();

        assertThat(emptyCustomer).isNull();
    }

    public CustomerDTO getSavedCustomerDto() {
        return customerService.saveCustomer(getTestCustomerDto()).block();
    }

    public static CustomerDTO getTestCustomerDto() {
        return new CustomerMapperImpl().customerToCustomerDto(getTestCustomer());
    }

    public static Customer getTestCustomer() {
        return Customer.builder()
                .customerName("José")
                .build();
    }
}