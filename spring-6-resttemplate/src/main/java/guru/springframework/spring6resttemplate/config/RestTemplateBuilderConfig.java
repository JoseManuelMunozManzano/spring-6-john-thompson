package guru.springframework.spring6resttemplate.config;

import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateBuilderConfig {

    // Sobreescribimos el comportamiento por defecto que SpringBoot autogenera, pero, como generalmente
    // la configuración por defecto es buena y hacerlo to-do es complicado, se usa el configurador
    // RestTemplateBuilderConfigurer.
    @Bean
    RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer) {

        // Lo que hace configurer es coger la nueva instancia y la configura usando los valores por defecto
        // de SpringBoot.
        RestTemplateBuilder builder = configurer.configure(new RestTemplateBuilder());

        // Luego, modificamos lo que necesitamos.
        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory("http://localhost:8080");

        // Importante devolver el builder llamando al handler del uri para devolver todos los valores del builder
        // incluido el cambiado.
        return builder.uriTemplateHandler(uriBuilderFactory);
    }
}
