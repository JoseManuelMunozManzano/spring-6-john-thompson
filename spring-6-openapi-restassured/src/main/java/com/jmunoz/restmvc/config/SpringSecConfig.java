package com.jmunoz.restmvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// Para los tests RestAssured, queremos deshabilitar Spring Security, ya que los tests no funcionan.
// Para ello, creamos un profile e indicamos !test.
// Atentos al !
// Este test profile indica que esta configuración no la cogemos si el profile de test está activo.
@Profile("!test")
@Configuration
public class SpringSecConfig {

    // Configuramos para que soporte ser un resource server que usa JWT.
    // Con esta configuración no hace falta deshabilitar CSRF.
    //
    // Añadimos configuración para ver los nuevos endpoints que se van a exponer
    // gracias a OpenAPI (los de Swagger y OpenAPI)
    // No queremos que estén securizados.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers("/v3/api-docs**", "/swagger-ui/**", "/swagger-ui.html")
                    .permitAll()
                    .anyRequest()
                    .authenticated();
                })
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer ->
                        httpSecurityOAuth2ResourceServerConfigurer.jwt(Customizer.withDefaults()));

        return http.build();
    }
}
