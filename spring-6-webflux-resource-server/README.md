# spring-6-webflux-resource-server

Vamos a coger el servidor de Spring reactivo `spring-6-webflux-rest-exception-handling` y se va a añadir OAuth, haciéndolo un OAuth Resource Server para realizar tareas de autorización

Para no crear otro proyecto, porque es muy sencillo, añadimos Spring Boot Actuator, Logbook y Logstash aquí mismo.

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

Se ha añadido la configuración de seguridad en `SecurityConfig.java`.

4. Logbook

Para trabajar con Logbook solo hay que añadir las dependencias

```
<properties>
    <logbook.version>3.9.0</logbook.version>
</properties>

<dependency>
    <groupId>org.zalando</groupId>
    <artifactId>logbook-spring-boot-starter</artifactId>
    <version>${logbook.version}</version>
</dependency>

<dependency>
    <groupId>org.zalando</groupId>
    <artifactId>logbook-spring-boot-webflux-autoconfigure</artifactId>
    <version>${logbook.version}</version>
</dependency>
```

Y la siguiente property en application.properties: `logging.level.org.zalando.logbook=trace`.

Hay que ejecutar también el proyecto `spring-6-auth-server` porque nos hace falta el token OAuth2.0.

5. Logstash

Sirve para habilitar JSON logging. Hay que añadir las siguientes dependencias

```
<properties>
    <logstash-logback-encoder.version>8.0</logstash-logback-encoder.version>
</properties>

<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>${logstash-logback-encoder.version}</version>
</dependency>
```

Y para configurarlo, se crea dentro de la carpeta `resources` el fichero `logback-spring.xml` y se escribe dicha configuración.

6. Que Logbook y Logstash trabajen juntos

Hay que añadir la siguiente dependencia

```
<dependency>
    <groupId>org.zalando</groupId>
    <artifactId>logbook-logstash</artifactId>
    <version>${logbook.version}</version>
</dependency>
```

Y se crea la siguiente configuración en el package `config`, con nombre `LogbookConfig.java`.

La idea es poder obtener en consola el JSON Payload en formato JSON, no String, y con el formato sin escapar.

Con esto, podemos buscar problemas de ejecución y realizar auditorías.

## Testing

- Clonar el repositorio
- En la carpeta `resources` existe el archivo `schema.sql` que se ejecuta automáticamente al arrancar el proyecto, gracias a la clase de configuración `DatabaseConfig.java`
- Ejecutar el proyecto `spring-6-auth-server`
    - Obtener el token usando el endpoint que está en su carpeta `postman`, y, una vez obtenido el token, pulsar `Use Token`
- Ejecutar este proyecto
- En la carpeta `postman` se encuentra un archivo que hay que importar a Postman e ir ejecutando
  - Como todos los endpoints están securizados, en todos hay que ir a la pestaña `Authorization`, indicar en Auth Type el valor OAuth 2.0 y en la parte derecha indicar el token obtenido
  - Una vez hecho indicado el token en los endpoints, ya se pueden ejecutar
- Para probar `Spring Boot Actuator`, ejecutar este proyecto e ir a Postman
  - En la carpeta `/postman/actuator` tenemos los distintos endpoints
- Para probar `Logbook` solo hay que ejecutar el proyecto
  - Ejecutar también el proyecto `spring-6-auth-server`
    - Obtener un token usando el endpoint que está en su carpeta `postman`, y, una vez obtenido el token, pulsar `Use Token`
  - En la carpeta `postman` tenemos los distintos endpoints. Copiar el token en cada uno y probar
  - Debe verse la traza con la request en la consola de ejecución del proyecto MVC, en formato String
  - Debe verse la traza con la response en la consola de ejecución del proyecto MVC, en formato String
- Para probar `Logbook` junto con `Logstash` solo hay que ejecutar el proyecto
  - Ejecutar también el proyecto `spring-6-auth-server`
    - Obtener un token usando el endpoint que está en su carpeta `postman`, y, una vez obtenido el token, pulsar `Use Token`
  - En la carpeta `postman` tenemos los distintos endpoints. Copiar el token en cada uno y probar
  - Debe verse la traza con la request en la consola de ejecución del proyecto MVC, ahora en formato JSON
  - Debe verse la traza con la response en la consola de ejecución del proyecto MVC, ahora en formato JSON