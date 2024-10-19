package guru.springframework.spring6resttemplate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestTemplateBuilderConfigurer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

@Configuration
public class RestTemplateBuilderConfig {

    // Usamos la anotación @Value para externalizar por completo código hardcode, usando para ello properties añadidas
    // al fichero application.properties.
    @Value("${rest.template.rootUrl}")
    String rootUrl;

    // Necesitamos implementar un Authorize Client Manager, que es un nuevo componente de Spring que obtenemos
    // de la dependencia añadida.
    // Maneja por nosotros las llamadas al Authorization Server, para obtener el token JWT.
    //
    // Este componente lo vamos a usar en conjunción con el RestTemplateBuilder.
    // Podríamos añadir este Bean a una clase de configuración separada.
    @Bean
    OAuth2AuthorizedClientManager auth2AuthorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
                                                               OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        // Vemos que hace falta un provider y las properties de configuración (application.properties)
        // se van a enlazar a este provider.
        var authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        var authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository, oAuth2AuthorizedClientService);

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    // Sobreescribimos el comportamiento por defecto que SpringBoot autogenera, pero, como generalmente
    // la configuración por defecto es buena y hacerlo to-do es complicado, se usa el configurador
    // RestTemplateBuilderConfigurer.
    //
    // También usamos aquí nuestro interceptor
    @Bean
    RestTemplateBuilder restTemplateBuilder(RestTemplateBuilderConfigurer configurer,
                                            OAuthClientInterceptor interceptor) {

        // Para asegurarnos de que la propiedad está cargada.
        // Si no lo está fallará al comenzar la ejecución.
        assert rootUrl != null;

        // Para poder trabajar con Logbook, añadimos este interceptor.
        // Se pueden configurar muchas cosas en el builder, pero aquí solo vamos a trabajar con la base.
        LogbookClientHttpRequestInterceptor logbookClientHttpRequestInterceptor =
                new LogbookClientHttpRequestInterceptor(Logbook.builder().build());

        RestTemplateBuilder builder = new RestTemplateBuilder();

        DefaultUriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory(rootUrl);

        return configurer.configure(builder)
                .additionalInterceptors(interceptor, logbookClientHttpRequestInterceptor)
                .uriTemplateHandler(uriBuilderFactory);
    }
}
