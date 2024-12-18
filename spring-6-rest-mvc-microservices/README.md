# spring-6-rest-mvc-microservices

A partir de `spring-6-rest-mvc-orders` añadimos a Beer Order Entity el campo `paymentAmount` para disparar el flujo de trabajo de los microservicios.

También modificamos nuestro Dto.

Añadimos una nueva migración de Flyway para añadir el campo a la BD.

También añadimos a Beer Order Line Entity el campo `status` para disparar el flujo de trabajo de los microservicios.

También modificamos nuestro Dto.

Añadimos una nueva migración de Flyway para añadir el campo a la BD.

Uso de Kafka. Ver también curso de Kafka: `https://github.com/JoseManuelMunozManzano/Spring-Boot-Kafka`

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

4. Kafka Dependencies and Configuration

Añadimos las siguientes dependencias de Kafka:

```
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka-test</artifactId>
</dependency>
```

Y añadimos en `application.properties`:

```
# Kafka - consumer and producer
spring.kafka.consumer.group-id=sfg
spring.kafka.consumer.auto-offset-reset=earliest
spring,kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.trusted.packages=*

spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
```

No tiene nada que ver con el proyecto, pero sí con que los logs sean más fáciles de leer el hecho de haber renombrado el fichero `logback-spring.xml` a `logback-spring.xml.old` para que no lo coja.

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql` 
