# spring-6-csv

Es el mismo ejemplo `spring-6-flyway` pero añadiendo subidas de ficheros CSV.

La idea es poblar con data la BD y, más adelante, añadir la paginación y ordenación.

Se va a usar el paquete `OpenCSV`. Nos ayudará a mapear la fila de data CSV a un POJO Java. Este POJO Java, a su vez, poblará la BD usando Spring Data JPA.

## Notas

1. En la carpeta `resources` se ha creado la carpeta `csvdata` y dentro se ha colocado el archivo `beers.csv`.

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql`
- Ejecutar los tests