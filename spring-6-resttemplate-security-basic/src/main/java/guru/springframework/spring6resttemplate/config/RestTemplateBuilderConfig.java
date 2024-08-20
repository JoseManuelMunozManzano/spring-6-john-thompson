package guru.springframework.spring6resttemplate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class RestTemplateBuilderConfig {

    @Value("${rest.template.username}")
    String username;

    @Value("${rest.template.password}")
    String password;

    // Usamos la anotación @Value para externalizar por completo código hardcode, usando para ello properties añadidas
    // al fichero application.properties.
    @Value("${rest.template.rootUrl}")
    String rootUrl;

    // Sobreescribimos el comportamiento por defecto que SpringBoot autogenera, pero, como generalmente
    // la configuración por defecto es buena y hacerlo to-do es complicado, se usa el configurador
    // RestTemplateBuilderConfigurer.
    @Bean
    RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer) {

        // Para asegurarnos de que la propiedad está cargada.
        // Si no lo está fallará al comenzar la ejecución.
        //
        // assert rootUrl != null;

        // Lo que hace configurer es coger la nueva instancia y la configura usando los valores por defecto
        // de SpringBoot.
        //
        // RestTemplateBuilder builder = configurer.configure(new RestTemplateBuilder());

        // Luego, modificamos lo que necesitamos.
        //
        // DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(rootUrl);

        // Añadimos HTTP Basic Authentication para añadir la seguridad y que la llamada a nuestros endpoints
        // del proyecto de backend funcionen correctamente.
        // No olvidar cada vez que llamamos a builder.<lo que sea> se crea un nuevo builder. Tenemos que
        // recuperar esa instancia en una variable para que funcione correctamente.
        //
        // RestTemplateBuilder builderWithAuth = builder.basicAuthentication(username, password);

        // Importante devolver el builderWithAuth llamando al handler del uri para devolver todos los valores
        // del builder incluido to-do lo añadido, como la seguridad.
        //
        // return builderWithAuth.uriTemplateHandler(uriBuilderFactory);

        // Refactorizamos a:
        assert rootUrl != null;

        RestTemplateBuilder builder = new RestTemplateBuilder();

        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(rootUrl);

        return configurer.configure(builder)
                .basicAuthentication(username, password)
                .uriTemplateHandler(uriBuilderFactory);
    }
}
