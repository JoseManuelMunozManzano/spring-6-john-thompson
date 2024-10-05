package com.jmunoz.reactivemongo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

// Con esta configuración (ver también application.properties) configuramos nuestra aplicación
// para ser un recurso OAuth2 server.
@Configuration
@EnableWebFluxSecurity
public class SpringSecConfig {

    @Bean
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
