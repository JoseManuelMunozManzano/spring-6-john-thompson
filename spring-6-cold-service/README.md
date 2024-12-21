# spring-6-cold-service

Este proyecto trabaja en conjunción con `spring-6-rest-mvc-api`, `spring-6-rest-mvc-microservices`, `spring-6-icecold-service` y `spring-6-cool-service`.

## Notas

1. Añadimos al POM la dependencia del proyecto `spring-6-rest-mvc-api`, que estará en nuestra carpeta `.m2`.

```
<dependency>
    <groupId>guru.springframework</groupId>
    <artifactId>spring-6-rest-mvc-api</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

2. Para cada microservicio hacer

- Configurar Kafka
- Crear un Listener para DrinkRequestEvent
- Crear un Service para “procesar” DrinkRequestEvent
- Crear un producer para publicar DrinkPreparedEvent de vuelta al Kafka topic
    - Usar el topic “drink.prepared”
- Crear Test para enviar DrinkRequest a Kafka y verificar que se recibe el mensaje en el topic “drink.prepared”

## Testing

- Clonar el repositorio
- Ejecutar el test `DrinkrequestListenerTest.java`