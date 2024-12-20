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
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
spring.kafka.consumer.properties.spring.deserializer.value.delegate.class=org.springframework.kafka.support.serializer.JsonDeserializer
spring.kafka.consumer.properties.spring.json.value.default.type=guru.springframework.spring6restmvcapi.model.BeerOrderDto
spring.kafka.consumer.properties.spring.json.trusted.packages=*

spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
```

No tiene nada que ver con el proyecto, pero sí con que los logs sean más fáciles de leer el hecho de haber renombrado el fichero `logback-spring.xml` a `logback-spring.xml.old` para que no lo coja.

5. Testing with Embedded Kafka

Vamos a enviar un mensaje a un topic de Kafka y asegurarnos de que se recibe.

Creamos una clase Kafka de configuración en `config/KafkaConfig.java`.

En `listeners/OrderPlacedListener.java` indicamos el método `listen()`.

Y creamos los tests `listeners/OrderPlacedListenerTest.java` y `listeners/OrderPlacedKafkaListener.java`.

6. Create Order Splitter Router

En el proyecto `spring-6-rest-mvc-api` hemos creado las clases `DrinkRequestEvent` y `DrinkPreparedEvent`.

En este proyecto, creamos nuevos topics en `config/KafkaConfig.java`.

A falta de un sitio mejor, creamos en `listeners` la clase `DrinkSplitterRouter.java`.

Creamos la configuración de test `listeners/DrinkListenerKafkaConsumer.java` y modificamos el test `listeners/OrderPlacedListenerTest.java`.

7. Create Spring Boot Kafka Microservices

Vamos a crear un microservicio para Kafka por cada servicio, en el que cada servicio va a ser tratado como un proyecto independiente.

Compartiremos su biblioteca API.

Ver proyectos `spring-6-icecold-service`, `spring-6-cold-service` y `spring-6-cool-service`.

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql` 
    - Ejecutar el test `listeners/OrderPlacedListenerTest.java`