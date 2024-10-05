# spring-6-webflux-fn-resource-server

Es continuación del proyecto `spring-6-webflux-fn` al que se le añade OAuth2, para convertirlo en un Resource Server.

## Notas

1. Añadir al POM la dependencia de OAuth2

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

## Testing

- Clonar el repositorio
- Renombrar `application.template.properties` a `application.properties` e indicar sus valores
- Ejecutar el proyecto `spring-6-auth-server`
    - Obtener el token usando el endpoint que está en su carpeta `postman`, y, una vez obtenido el token, pulsar `Use Token`
- Ejecutar este proyecto
- En la carpeta `postman` se encuentra un archivo que hay que importar a Postman e ir ejecutando
    - Como todos los endpoints están securizados, en todos hay que ir a la pestaña `Authorization`, indicar en Auth Type el valor OAuth 2.0 y en la parte derecha indicar el token obtenido
    - Una vez hecho indicado el token en los endpoints, ya se pueden ejecutar