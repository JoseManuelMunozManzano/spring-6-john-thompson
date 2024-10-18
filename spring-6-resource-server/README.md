# spring-6-resource-server

Usando las credenciales de OAuth2 generadas en el proyecto anterior `spring-6-auth-server`, vamos a dar seguridad a nuestro RESTful API

Partimos del proyecto `spring-6-security-basic` y vamos a usar Spring Security para configurarlo como un OAuth2 Resource Server, es decir, que va a aceptar un Token JWT que obtendremos del Authentication Server y usarlo para dar seguridad a nuestras APIs

Para no crear otro proyecto, porque es muy sencillo, añadimos Spring Boot Actuator y Logbook aquí mismo.

## Notas

1. Necesitamos añadir al POM la dependencia de resource-server

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

2. Añadir la dependencia para Spring Boot Actuator

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Se ha añadido la configuración de seguridad en `SpringSecConfig.java`.

3. Añadir Integración de Actuator para las sondas en Kubernates

Se hace vía properties.

```
management.endpoint.health.probes.enabled=true
management.health.readinessstate.enabled=true
management.health.livenessstate.enabled=true
```

4. Logbook

Para trabajar con Logbook solo hay que añadir la dependencia

```
<properties>
    <logbook.version>3.9.0</logbook.version>
</properties>

<dependency>
    <groupId>org.zalando</groupId>
    <artifactId>logbook-spring-boot-starter</artifactId>
    <version>${logbook.version}</version>
</dependency>
```

Y la siguiente property en application.properties: `logging.level.org.zalando.logbook=trace`.

Hay que ejecutar también el proyecto `spring-6-auth-server` porque nos hace falta el token OAuth2.0.

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql`
  - Importante si queremos usar MySql en vez de H2
- Ejecutar también el proyecto `spring-6-auth-server`
  - Obtener un token usando el endpoint que está en su carpeta `postman`, y, una vez obtenido el token, pulsar `Use Token`
  - Ejecutar el endpoint GET pulsando el botón `Send`
  - Debe aparecer todo el listado de cervezas
  - Si eliminamos algún carácter del token, veremos que da error 401, Unauthorized
- Ejecutar los tests de nuestro proyecto `spring-6-resource-server`
  - Para esto no hace falta ejecutar el proyecto `spring-6-auth-server`
- Para probar `Spring Boot Actuator`, ejecutar este proyecto e ir a Postman
  - En la carpeta `/postman/actuator` tenemos los distintos endpoints
- Para probar `Logbook` solo hay que ejecutar el proyecto
  - Ejecutar también el proyecto `spring-6-auth-server`
    - Obtener un token usando el endpoint que está en su carpeta `postman`, y, una vez obtenido el token, pulsar `Use Token`
  - En la carpeta `postman` tenemos los distintos endpoints. Copiar el token en cada uno y probar
  - Debe verse la traza con la request en la consola de ejecución del proyecto MVC
  - Debe verse la traza con la response en la consola de ejecución del proyecto MVC
