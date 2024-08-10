# spring-6-db-relationships

Es el mismo ejemplo `spring-6-paging-sorting` pero ahora vamos a mapear relaciones entre tablas.

Esto añade un nivel de complejidad, porque vamos a añadir tablas adicionales y usar Flyway para realizar las migraciones.

El mapeo de relaciones en JPA e Hibernate está muy maduro, es muy versátil y puede manejar desde BD standards hasta BD extrañas y muy antiguas.

## Notas

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql`
- Ejecutar todos los tests