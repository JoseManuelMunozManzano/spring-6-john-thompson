package com.jmunoz.reactivemongo.config;

import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

// Con esta configuración (ver también application.properties) configuramos nuestra aplicación
// para ser un recurso OAuth2 server.
@Configuration
@EnableWebFluxSecurity
public class SpringSecConfig {

    // Este bean es para configurar la seguridad en el endpoint de Actuator.
    // Por defecto solo el endpoint health va a ser habilitado.
    @Bean
    @Order(1)
    public SecurityWebFilterChain actuatorSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        http.securityMatcher(EndpointRequest.toAnyEndpoint())
                .authorizeExchange(authorize -> authorize.anyExchange().permitAll());

        return http.build();
    }

    @Bean
    @Order(2)
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        // Securizamos todos los endpoints
        //
        // Se añade la parte de deshabilitar CSRF para que funcionen los tests.
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorize -> authorize.anyExchange().authenticated())
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()));

        return http.build();
    }
}
