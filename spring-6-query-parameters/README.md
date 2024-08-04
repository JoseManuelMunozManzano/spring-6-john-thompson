# spring-6-query-parameters

Es el mismo ejemplo `spring-6-csv` pero las peticiones ahora vienen con query parameters.

La idea es utilizar Spring MVC para que acepte query parametesrs, y devolver y consultar data de BD usando Spring Data JPA.

## Notas

1. Un query parameter viene al final de la URL, tras la marca ?, seguido del nombre del query parameter, la marca = y el valor.

Ejemplo: http://localhost:8080/api/v1/beer?beerStyle=ALE

Hay que configurar Spring MVC para que lo parsee.

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql`
- Ejecutar los tests
