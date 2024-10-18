# spring-6-webflux-fn-resource-server

Es continuación del proyecto `spring-6-webflux-fn` al que se le añade OAuth2, para convertirlo en un Resource Server.

Para no crear otro proyecto, porque es muy sencillo, añadimos Spring Boot Actuator aquí mismo.

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

3. Añadir la dependencia para Spring Boot Actuator

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Se ha añadido la configuración de seguridad en `SpringSecConfig.java`.

4. Añadir Integración de Actuator para las sondas en Kubernates

Se hace vía properties.

```
management.endpoint.health.probes.enabled=true
management.health.readinessstate.enabled=true
management.health.livenessstate.enabled=true
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
- Ejecutar los tests (para estos tests no hace falta ejecutar ninguno de los dos proyectos)
- Para probar `Spring Boot Actuator`, ejecutar este proyecto e ir a Postman
  - En la carpeta `/postman/actuator` tenemos los distintos endpoints