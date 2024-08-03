# spring-6-csv

Es el mismo ejemplo `spring-6-flyway` pero añadiendo subidas de ficheros CSV.

La idea es poblar con data la BD y, más adelante, añadir la paginación y ordenación.

Se va a usar el paquete `OpenCSV`. Nos ayudará a mapear la fila de data CSV a un POJO Java. Este POJO Java, a su vez, poblará la BD usando Spring Data JPA.

## Notas

1. En la carpeta `resources` se ha creado la carpeta `csvdata` y dentro se ha colocado el archivo `beers.csv`.

2. Se crea un service cuya única función es convertir la data CSV en un Java POJO.

También se crea su clase de test.

3. Para cargar la data del CSV en BD, lo mejor es hacerlo desde la capa `bootstrap`.

En un proyecto real, puede que se obtenga el fichero CSV desde un directorio del file system. Podemos tener un trabajo planificado que se encargue de revisar si existe ese fichero.

En este proyecto educativo, la idea es ver como se persiste la data en BD.

4. Vemos como Hibernate automáticamente puebla los campos de auditoría `createdDate` y `updatedDate`.

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql`
  - Se debe persistir en BD, en la tabla `beer`, toda la data del fichero .CSV
- Ejecutar los tests
  - BeerCSVServiceImplTest.java