# spring-6-db-transactions

Es el mismo ejemplo `spring-6-db-relationships` pero ahora vamos a ver un ejemplo de fallo de bloqueo optimista.

## Notas

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql`
  - Importante si queremos usar MySql en vez de H2
- Se ha hecho el ejemplo en el test `BeerControllerIT.java`, en concreto en el método `testUpdateBeerBadVersion()`