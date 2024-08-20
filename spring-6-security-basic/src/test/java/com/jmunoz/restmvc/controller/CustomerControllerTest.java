package com.jmunoz.restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmunoz.restmvc.config.SpringSecConfig;
import com.jmunoz.restmvc.model.CustomerDto;
import com.jmunoz.restmvc.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Security: Para que no fallen los endpoints distintos a GET (como POST, PUT...) hemos creado una clase de
// configuración que tenemos que importar.
@WebMvcTest(CustomerController.class)
@Import(SpringSecConfig.class)
class CustomerControllerTest {

    @Value("${spring.security.user.name}")
    private String user;

    @Value("${spring.security.user.password}")
    private String password;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustomerService customerService;

    // Vemos una segunda forma de implementar ArgumentCaptor.
    // Lo bueno de esta forma es que se puede reutilizar.
    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<CustomerDto> customerArgumentCaptor;

    List<CustomerDto> testCustomers;

    @BeforeEach
    void setUp() {
        CustomerDto jm = CustomerDto.builder()
                .id(UUID.randomUUID())
                .name("José Manuel")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CustomerDto adri = CustomerDto.builder()
                .id(UUID.randomUUID())
                .name("Adriana")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CustomerDto marina = CustomerDto.builder()
                .id(UUID.randomUUID())
                .name("Marina")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        testCustomers = Arrays.asList(jm, adri, marina);
    }

    // Lanzamiento de excepción usando Mockito.
    //
    // Esta vez lo vamos a manejar usando @ControllerAdvice, para configurar un manejador de excepción global (recordar
    // que @ExceptionHandler es local al controller donde está definido)
    //
    // Pero como ahora hemos comentado esa parte, vemos que también de forma global, es manejado por @ResponseStatus.
    @Test
    void getCustomerByIdNotFound() throws Exception {

        // El problema es que estamos buscando en el service para lanzar la excepción.
        // Se corrige usando el Optional de Java, de forma que ahora el service solo devuelve null y es el controller
        // el que lanza la excepción.
        // given(customerService.getCustomerById(any(UUID.class))).willThrow(NotFoundException.class);
        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/customer/" + testCustomers.getFirst().getId())
                        .with(httpBasic(user, password)))
                .andExpect(status().isNotFound());
    }

    @Test
    void listCustomers() throws Exception {
        given(customerService.listCustomers()).willReturn(testCustomers);

        mockMvc.perform(get("/api/v1/customer")
                        .with(httpBasic(user, password))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getCustomerById() throws Exception {
        given(customerService.getCustomerById(testCustomers.getFirst().getId())).willReturn(Optional.of(testCustomers.getFirst()));

        mockMvc.perform(get("/api/v1/customer/" + testCustomers.getFirst().getId())
                        .with(httpBasic(user, password))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testCustomers.getFirst().getId().toString())))
                .andExpect(jsonPath("$.name", is(testCustomers.getFirst().getName())));
    }

    @Test
    void testCreateNewCustomer() throws Exception {
        CustomerDto customer = testCustomers.getFirst();
        customer.setId(null);
        customer.setVersion(null);

        given(customerService.saveNewCustomer(any(CustomerDto.class))).willReturn(testCustomers.get(1));

        mockMvc.perform(post("/api/v1/customer")
                        .with(httpBasic(user, password))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        CustomerDto customer = testCustomers.getFirst();

        given(customerService.updateCustomerById(any(), any())).willReturn(Optional.of(customer));

        mockMvc.perform(put("/api/v1/customer/" + customer.getId())
                        .with(httpBasic(user, password))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isNoContent());

        // En este caso, en vez de usar eq(customer.getId()) he usado any
        // verify(customerService).updateCustomerById(any(UUID.class), any(Customer.class));

        // Reutilizando ArgumentCaptor
        verify(customerService).updateCustomerById(uuidArgumentCaptor.capture(), any(CustomerDto.class));
        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testDeleteCustomer() throws Exception {
        CustomerDto customer = testCustomers.getFirst();

        given(customerService.deleteCustomerById(any())).willReturn(true);

        mockMvc.perform(delete("/api/v1/customer/" + customer.getId())
                        .with(httpBasic(user, password))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Usando ArgumentCaptor (inyectado) en vez de eq() o any()
        verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());
        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testPatchCustomer() throws Exception {
        CustomerDto customer = testCustomers.getFirst();

        given(customerService.patchCustomerById(any(), any())).willReturn(Optional.of(customer));

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("name", "New Name");

        mockMvc.perform(patch("/api/v1/customer/" + customer.getId())
                        .with(httpBasic(user, password))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(customer.getId());
        assertThat(customerArgumentCaptor.getValue().getName()).isEqualTo(customerMap.get("name"));
    }
}