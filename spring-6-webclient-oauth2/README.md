# spring-6-webclient-oauth2

Vamos a añadir OAuth2 al proyecto `spring-6-webclient` para convertirlo en un cliente con OAuth2.

Básicamente, esto sustituye a Postman a la hora de ejecutar los endpoints del proyecto `spring-6-webflux-fn-resource-server`.

En concreto, del proyecto `spring-6-auth-server` obtenemos un JWT Token y lo utilizamos para trabajar con el resource server `spring-6-webflux-fn-resource-server` para exponer los recursos que usa el WebClient.

## Notas

1. Añadir al POM la dependencia de OAuth2, para ser un cliente

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

```
# Configuración estándar de Spring Security para el cliente OAuth2
# En provider podemos indicar el nombre que queramos

spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:9000
spring.security.oauth2.client.registration.springauth.client-id=<client_id>
spring.security.oauth2.client.registration.springauth.client-secret=<client_secret>
spring.security.oauth2.client.registration.springauth.scope[0]=message.read
spring.security.oauth2.client.registration.springauth.scope[1]=message.write
spring.security.oauth2.client.registration.springauth.authorization-grant-type=client_credentials
spring.security.oauth2.client.registration.springauth.provider=<nombre_1>

# Vemos provider.<nombre_1> porque tiene que ser el mismo nombre que se indicó arriba en provider
spring.security.oauth2.client.provider.<nombre_1>.authorization-uri=http://localhost:9000/auth2/authorize
spring.security.oauth2.client.provider.<nombre_1>.token-uri=http://localhost:9000/oauth2/token
```

2. Necesitamos implementar un Authorize Client Manager, que es un nuevo componente de Spring que obtenemos de la dependencia añadida.

Maneja por nosotros las llamadas al Authorization Server, para obtener el token JWT.

3. También se actualiza `WebClientConfig.java` para trabajar con ese Authorize Client Manager.

4. Logbook

Para trabajar con Logbook solo hay que añadir la dependencia

```
<properties>
    <logbook.version>3.9.0</logbook.version>
</properties>

<dependency>
    <groupId>org.zalando</groupId>
    <artifactId>logbook-spring-webflux</artifactId>
    <version>${logbook.version}</version>
</dependency>
```

Hay que realizar configuración, en el package `config`, fuente `WebClientConfig.java` para añadir un filtro.

Y la siguiente property en application.properties: `logging.level.org.zalando.logbook=trace`.

En clientes, es muy útil para testear lo que se pasa a través de la red, para saber lo que hace nuestro cliente.

## Testing

- Clonar el repositorio
- Renombrar `application.template.properties` a `application.properties` e indicar sus valores
- Ejecutar los tests de la clase `BeerClientImplTest.java`
  - Tienen que estar en ejecución los siguientes proyectos: `spring-6-auth-server`, `spring-6-gateway` y `spring-6-webflux-fn-resource-server`
- Para probar `Logbook` vamos a ejecutar el test `BeerClientImplTest`, método `tesUpdateBeer()`
  - Como no es un mock, deben ejecutarse los proyectos: `spring-6-auth-server`, `spring-6-gateway` y `spring-6-webflux-fn-resource-server`  
  - Debe verse la traza de la petición y la respuesta