# spring-6-webflux-resource-server

Vamos a coger el servidor de Spring reactivo `spring-6-webflux-rest-exception-handling` y se va a añadir OAuth, haciéndolo un OAuth Resource Server para realizar tareas de autorización

## Notas

1. Añadir al POM la dependencia de OAuth2

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

2. Para que no se rompa el testing al añadir OAuth2 hay que añadir al POM

```
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
	<scope>test</scope>
</dependency>

<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-oauth2-client</artifactId>
    <scope>test</scope>
</dependency>
```

También tenemos que modificar `BeerControllerTest.java` y `CustomerControllerTest.java` para añadir:

```
.mutateWith(mockOAuth2Login())
```

Y `SecurityConfig.java` para añadir:

```
.csrf(ServerHttpSecurity.CsrfSpec::disable)
```

## Testing

- Clonar el repositorio
- En la carpeta `resources` existe el archivo `schema.sql` que se ejecuta automáticamente al arrancar el proyecto, gracias a la clase de configuración `DatabaseConfig.java`
- Ejecutar el proyecto `spring-6-auth-server`
    - Obtener el token usando el endpoint que está en su carpeta `postman`, y, una vez obtenido el token, pulsar `Use Token`
- Ejecutar este proyecto
- En la carpeta `postman` se encuentra un archivo que hay que importar a Postman e ir ejecutando
  - Como todos los endpoints están securizados, en todos hay que ir a la pestaña `Authorization`, indicar en Auth Type el valor OAuth 2.0 y en la parte derecha indicar el token obtenido
  - Una vez hecho indicado el token en los endpoints, ya se pueden ejecutar