# spring-6-rest-mvc-microservices

A partir de `spring-6-rest-mvc-orders` añadimos a Beer Order Entity el campo `paymentAmount` para disparar el flujo de trabajo de los microservicios.

También modificamos nuestro Dto.

Añadimos una nueva migración de Flyway para añadir el campo a la BD.

También añadimos a Beer Order Line Entity el campo `status` para disparar el flujo de trabajo de los microservicios.

También modificamos nuestro Dto.

Añadimos una nueva migración de Flyway para añadir el campo a la BD.

## Notas

1. En BD, lo que mejor emula un Big Decimal es NUMERIC.

2. Se añade al POM la dependencia del proyecto creado `spring-6-rest-mvc-api`, que estará en nuestra carpeta `.m2`.

```
<dependency>
    <groupId>guru.springframework</groupId>
    <artifactId>spring-6-rest-mvc-api</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

3. Refactorizamos el proyecto, eliminando todos los DTO, puesto que los va a coger de nuestro proyecto `spring-6-rest-mvc-api`.

Importante, una vez refactorizado, ejecutar, del ciclo de vida de Maven, el goal `clean` y luego `compile`.

Queremos que, al actualizar aquí el campo `paymentAmount` se dispare la creación de `OrderPlacedEvent` en `spring-6-rest-mvc-api`.

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql` 
