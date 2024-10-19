# spring-6-resource-server

Usando las credenciales de OAuth2 generadas en el proyecto anterior `spring-6-auth-server`, vamos a dar seguridad a nuestro RESTful API

Partimos del proyecto `spring-6-security-basic` y vamos a usar Spring Security para configurarlo como un OAuth2 Resource Server, es decir, que va a aceptar un Token JWT que obtendremos del Authentication Server y usarlo para dar seguridad a nuestras APIs

Para no crear otro proyecto, porque es muy sencillo, añadimos Spring Boot Actuator, Logbook y Logstash aquí mismo.

También vamos a añadir caché al proyecto. Cashing es el proceso de almacenar data en una localización temporal, con la idea de mejorar el rendimiento.

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

7. Cashing

Para añadir tratamiento de caché a nuestro proyecto, necesitamos añadir la siguiente dependencia

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

Necesitamos habilitar este cashing.

Como no tenemos ninguna clase de configuración de cashing, vamos a habilitarlo en la clase principal, `RestmvcApplication.java` para toda la aplicación.

Esto crea una sencilla implementación de caché por defecto.

Veremos también como habilitar caché en métodos concretos.

8. Cashing in methods

Pensando que el cashing es como un map en memoria, tenemos que indicar que map queremos usar.

Indicamos como map, beerCache en nuestro fichero de properties: `spring.cache.cache-name=beerCache`.

Ahora tenemos que habilitar los métodos en los que queremos usar esta caché. Lo hacemos en `services/BeerServiceJPA.java` añadiendo al método:

```
@Cacheable(cacheNames = "beerCache", key = "#id")
```

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
  - Debe verse la traza con la request en la consola de ejecución del proyecto MVC, en formato String
  - Debe verse la traza con la response en la consola de ejecución del proyecto MVC, en formato String
- Para probar `Logbook` junto con `Logstash` solo hay que ejecutar el proyecto
  - Ejecutar también el proyecto `spring-6-auth-server`
    - Obtener un token usando el endpoint que está en su carpeta `postman`, y, una vez obtenido el token, pulsar `Use Token`
  - En la carpeta `postman` tenemos los distintos endpoints. Copiar el token en cada uno y probar
  - Debe verse la traza con la request en la consola de ejecución del proyecto MVC, ahora en formato JSON
  - Debe verse la traza con la response en la consola de ejecución del proyecto MVC, ahora en formato JSON
- Para probar el cashing, tenemos que ejecutar este proyecto
  - Ejecutar también el proyecto `spring-6-auth-server`
    - Obtener un token usando el endpoint que está en su carpeta `postman`, y, una vez obtenido el token, pulsar `Use Token`
    - Ejecutar el endpoint `Get All Beers` usando el token, para obtener todos los ids
    - Ejecutar ahora el endpoint que realmente queremos y estamos cacheando, `Get Beer By Id` usando uno de los id que de la consulta anterior (y el token)
    - Vemos que tarda unos 60ms en ejecutarse y en consola aparece que se ha ejecutado el service
    - Si volvemos a ejecutar de nuevo el endpoint `Get Beer By Id` veremos que ahora tarda mucho menos, en mi ejemplo 13ms, y no se ha ejecutado la parte del service, porque gracias al cache no se le ha llamado siquiera