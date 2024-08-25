package com.jmunoz.restmvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecConfig {

    // Configuramos HTTP Basic Authentication
    //
    // Indicar que no deshabilitamos CSRF por completo, solo para los diferentes endpoints de la API, es decir,
    // to-do lo que comience con API.
    // En una hipotética aplicación web, sigue funcionando el control csrf (esto es un Rest Service)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            authorizationManagerRequestMatcherRegistry
                    .anyRequest()
                    .authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .csrf(httpSecurityCsrfConfigurer ->
                        httpSecurityCsrfConfigurer.ignoringRequestMatchers("/api/**"));

        return http.build();
    }
}
