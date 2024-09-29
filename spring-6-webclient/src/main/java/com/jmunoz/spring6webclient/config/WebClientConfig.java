package com.jmunoz.spring6webclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

// Establecemos la configuración de baseUrl.
// Ahora, al desplegar nuestra app, si necesitamos cambiar la baseUrl, cosa que es fácil que ocurra,
// solo tenemos que tocar application.properties.
@Configuration
public class WebClientConfig implements WebClientCustomizer {

    private final String rootUrl;

    public WebClientConfig(@Value("${webclient.rooturl}") String rootUrl) {
        this.rootUrl = rootUrl;
    }

    @Override
    public void customize(WebClient.Builder webClientBuilder) {
        webClientBuilder.baseUrl(rootUrl);
    }
}
