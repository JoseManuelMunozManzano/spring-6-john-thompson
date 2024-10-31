# spring-6-rest-mvc-microservices

A partir de `spring-6-rest-mvc-orders` añadimos a Beer Order Entity el campo `paymentAmount` para disparar el flujo de trabajo de los microservicios.

También modificamos nuestro Dto.

Añadimos una nueva migración de Flyway para añadir el campo a la BD.

## Notas

1. En BD, lo que mejor emula un Big Decimal es NUMERIC.

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql`
