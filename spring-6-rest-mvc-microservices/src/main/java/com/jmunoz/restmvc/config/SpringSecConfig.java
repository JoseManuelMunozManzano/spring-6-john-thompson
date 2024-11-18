package com.jmunoz.restmvc.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecConfig {

    // Este bean es para configurar la seguridad en el endpoint de Actuator.
    // Por defecto solo el endpoint health va a ser habilitado.
    @Bean
    @Order(1)
    public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {

        // Esta es la primera forma de configurar la seguridad en Actuator
        // http.authorizeHttpRequests(authorize -> authorize.requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll());

        // Esta es la segunda forma de configurar la seguridad en Actuator.
        // Esta es la forma recomendada en la documentación.
        http.securityMatcher(EndpointRequest.toAnyEndpoint())
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());

        return http.build();
    }

    // Configuramos para que soporte ser un resource server que usa JWT.
    // Con esta configuración no hace falta deshabilitar CSRF.
    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            authorizationManagerRequestMatcherRegistry
                    .anyRequest()
                    .authenticated();
                })
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer ->
                        httpSecurityOAuth2ResourceServerConfigurer.jwt(Customizer.withDefaults()));

        return http.build();
    }
}
