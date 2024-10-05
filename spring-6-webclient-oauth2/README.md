# spring-6-webclient-oauth2

Vamos a añadir OAuth2 al proyecto `spring-6-webclient` para convertirlo en un cliente con OAuth2

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

## Testing

- Clonar el repositorio
- Renombrar `application.template.properties` a `application.properties` e indicar sus valores