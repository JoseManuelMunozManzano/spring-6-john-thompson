package guru.springframework.spring6resttemplate.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import static java.util.Objects.isNull;

// Esta es la configuración de la instancia de un ClientHTTPRequestInterceptor
// La idea es que RestTemplate tenga un interceptor que valide si obtuvo autorización.
// Si no lo obtuvo lo va a añadir gracias también al Authentication Manager.
public class OAuthClientInterceptor implements ClientHttpRequestInterceptor {

    // Este es el manager que ya configuramos y del que obtenemos el bean.
    private final OAuth2AuthorizedClientManager manager;
    private final Authentication principal;
    private final ClientRegistration clientRegistration;

    public OAuthClientInterceptor(OAuth2AuthorizedClientManager manager, Authentication principal,
                                  ClientRegistration clientRegistration) {
        this.manager = manager;
        this.principal = principal;
        this.clientRegistration = clientRegistration;
    }

    // Este es el interceptor. Intercepta la request y trabaja con el ClientManager.
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // De nuevo, los valores de application.properties se usan aquí.
        OAuth2AuthorizeRequest oAuth2AuthorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId(clientRegistration.getClientId())
                .principal(createPrincipal())
                .build();

        // La request tiene información sobre a quién queremos autorizar. Enlazado a lo configurado en application.properties.
        // El manager obtiene el token JWT.
        OAuth2AuthorizedClient client = manager.authorize(oAuth2AuthorizeRequest);

        if (isNull(client)) {
            throw new IllegalStateException("Missing credentials");
        }

        // Añadimos al header: Authorization junto con "Bearer " y el token JWT.
        request.getHeaders().add(HttpHeaders.AUTHORIZATION,
                "Bearer " + client.getAccessToken());

        return execution.execute(request, body);
    }

    // Authentication es un componente estándar de Spring Security que mantiene información de seguridad sobre lo que se llama
    // un principal, una entidad que ha sido autorizada en el contexto de Spring Security.
    // Como vemos, es una interface que hay que implementar.
    private Authentication createPrincipal() {
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.emptySet();
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return this;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            }

            @Override
            public String getName() {
                return clientRegistration.getClientId();
            }
        };
    }
}
