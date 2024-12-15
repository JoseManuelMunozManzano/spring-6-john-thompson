# spring-6-rest-mvc-test-containers

Parte del ejemplo `spring-6-flyway`

- Nueva sección que aparece entre las secciones `Flyway Migrations with Spring Boot` y `CSV File Uploads`
- Los `Test containers` fueron añadidos a la versión 3.1 de Spring Boot
- Nos permite traer un contenedor Docker y automáticamente configurarlo para conectarlo a nuestra configuracieon de BBDD
- Van a arrancar en puertos aleatorios para evitar conflictos de puertos en el sistema host y, aunque el puerto cambia con cada ejecución, la configuración automática de Spring Boot recogerá los ajustes del contenedor Docker
- Nos permite añadir pruebas de integración con BBDD, message brokers, auth servers... que son portables y funcionarán en múltiples plataformas
- Necesitamos tener Docker instalado

Documentación: `https://docs.spring.io/spring-boot/reference/testing/testcontainers.html`

## Notas

1. Añadir las dependencias

Tenemos que añadir 3 dependencias:

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-testcontainers</artifactId>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>junit-jupiter</artifactId>
</dependency>

<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>mysql</artifactId>
</dependency>
```
2. Test Containers Dynamic Properties

Ver la clase de test `repositories/MySqlTest.java`.

Notar que se usan la anotaciones `@TestContainers` a nivel de clase y `@Container` y `@DynamicPropertySource` a nivel de método.

Lo que hacemos es sobreescribir propiedades de conexión de MySQL, en concreto username, password y url, para que use las del test container.

3. Using Service Connection with Test Containers

Ver la clase de test `repositories/MySqlTestServiceConnection.java`

En el punto 2 vimos una manera "difícil" de configurar los test containers.

Usando `@ServiceConnection` se simplifica mucho más toda esta configuración.

Lo que hace es marcar ese contenedor para, automáticamente, sobreescribir las propiedades de conexión.

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql`
  - Deben generarse las tablas `beer`, `customer` y un historial de flyway `flyway_schema_history` donde podemos ver en que estado está la BD, que scripts se han aplicado y cuales no.
- Ejecutar con debug el test `MySqlTest.java`
  - Hay que tener arrancado Docker (o Docker Desktop)
  - Al ejecutar el debug del test, si vamos a Docker, veremos una imagen `mysql`, y si vamos al container, veremos un puerto asignado random
  - Ver, del debug, `this.dataSource` cuyos valores `username`, `password` y `jdbcUrl` deben haber sido sustituidos por los del test container
- Ejecutar el test `MySqlTestServiceConnection.java` y ver que funciona