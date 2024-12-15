# spring-6-reactive-mongo

Nuevo proyecto en el que se prueba la conectividad de Spring 6 con la BBDD no relacional MongoDB usando programación reactiva.

Se añade también tests de integración con MongoDB usando test containers.

## Notas

1. Awaitility

Es una utilidad Java que nos sirve para realizar tests, y la usamos cuando el test termina antes de que se complete
un subscribe.

Hay que añadir esta dependencia al POM

```
<dependency>
    <groupId>org.awaitility</groupId>
    <artifactId>awaitility</artifactId>
    <version>4.2.1</version>
    <scope>test</scope>
</dependency>
```

Hay otro framework muy parecido en cuanto a funcionamiento llamado `StepVerifier`. Las diferencias serían:

One key difference between these two frameworks is that Awaitility is more focused on general asynchronous testing, while StepVerifier is specifically designed for testing reactive streams.

Another difference is that Awaitility relies on polling to check for completion of asynchronous operations, whereas StepVerifier is more deterministic, as it simulates events in the stream and waits for the expected results.

2. Query Methods usando MongoDB

`https://docs.spring.io/spring-data/mongodb/reference/repositories/query-methods-details.html#repositories.query-methods.query-creation`

3. Diferencia entre findBy... y findFirst...

Si se encuentra más de un registro, findBy lanza una excepción, mientras que findFirst devuelve el primero que pilla.

4. En este proyecto también tenemos una clase `BootstrapData.java` para inicializar la BD.

5. Refactor Database Configuration

Como vimos en el proyecto `spring-6-rest-mvc-test-containers`, a partir de Spring Boot 3.1 tenemos soporte para Docker y Docker Compose.

Vamos a refactorizar la configuración de la conexión a la BD Mongo para usar propiedades de Spring Boot.

Para ello vamos a la clase `config/MongoConfig.java` y eliminamos la anotación `@Configuration` para que no la utilice. Ejecutamos el ciclo Maven `clean`.

Si ejecutamos de nuevo el test `BeerServiceImplTest.java` veremos que sigue funcionando porque coge los valores de conexión a la BD Mongo directamente de `application.properties`

Con esto podemos borrar `config/MongoConfig.java`, pero la voy a dejar por motivos docentes, para saber como se construye.

6. Using Test Containers with MongoDB

Vamos a usar test containers para nuestra conexión de Mongo.

Tenemos que añadir las siguientes dependencias al pom:

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-testcontainers</artifactId>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>mongodb</artifactId>
    <scope>test</scope>
</dependency>
```

En el test `/services/BeerServiceImplTest.java` añadimos la anotación `@Testcontainers` a nivel de clase y `@Container` y `@ServiceConnection` a nivel de property.

7. Using Docker Compose with Spring Boot and MongoDB

Si queremos ejecutar nuestra app, podemos usar también Spring Boot Docker Compose.

Funciona parecido a los testcontainers, en el sentido de que enlaza los parámetros necesarios de configuración de BD a nuestra aplicación Spring Boot automáticamente.

Para configurarlo:

Añadimos las siguientes dependencias al fichero pom:

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-docker-compose</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

Donde, indicando el scope runtime y optional a true, decimos que que no queremos que se incluya esta dependencia en fichero .jar que se genere al hacer el bundle para deploy de nuestra aplicación.

Creamos, a nivel de raiz de proyecto, un archivo `compose.yaml`

Y ejecutamos el proyecto.

## Testing

- Clonar el repositorio
- Renombrar `application.template.properties` a `application.properties` e indicar sus valores
- Ejecutar el test `BeerServiceImplTest.java`
  - Tras el refactor realizado, va contra test containers, así que tira de Docker local

- Ejecutar el proyecto para ver como usa Docker Compose automáticamente
  - Tenemos que tener levantado Docker
  - Veremos lo siguiente en los logs de ejecución de nuestra aplicación:
    - `Using Docker Compose file .../compose.yaml`
    - En Docker Desktop veremos la imagen y el contenedor ejecutándose