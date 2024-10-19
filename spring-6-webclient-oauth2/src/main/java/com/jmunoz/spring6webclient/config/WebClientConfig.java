package com.jmunoz.spring6webclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.spring.webflux.LogbookExchangeFilterFunction;

// Establecemos la configuración de baseUrl.
// Ahora, al desplegar nuestra app, si necesitamos cambiar la baseUrl, cosa que es fácil que ocurra,
// solo tenemos que tocar application.properties.
@Configuration
public class WebClientConfig implements WebClientCustomizer {

    private final String rootUrl;
    private final ReactiveOAuth2AuthorizedClientManager authorizedClientManager;

    public WebClientConfig(@Value("${webclient.rooturl}") String rootUrl,
                           ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
        this.rootUrl = rootUrl;
        this.authorizedClientManager = authorizedClientManager;
    }

    @Override
    public void customize(WebClient.Builder webClientBuilder) {

        ServerOAuth2AuthorizedClientExchangeFilterFunction oAuth2AuthorizedClientExchangeFilterFunction =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);

        // Este paso de aquí no puede olvidarse porque si no, nada funciona.
        // El valor "springauth" es el valor del provider en application.properties.
        // En application.template.properties correspnde al nombre que se indique en <nombre_1>
        oAuth2AuthorizedClientExchangeFilterFunction
                .setDefaultClientRegistrationId("springauth");

        // Para poder trabajar con Logbook, añadimos este filtro.
        // Se pueden configurar muchas cosas en el builder, pero aquí solo vamos a trabajar con la base.
        LogbookExchangeFilterFunction logbookWebFilter = new LogbookExchangeFilterFunction(Logbook.builder().build());

        // Y ahora añadimos aquí el filtro.
        webClientBuilder
                .filter(oAuth2AuthorizedClientExchangeFilterFunction)
                .filter(logbookWebFilter)
                .baseUrl(rootUrl);
    }
}
