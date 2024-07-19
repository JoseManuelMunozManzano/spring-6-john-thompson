# spring-6-rest-mvc

Vamos a trabajar con servicios REST MVC y con el proyecto Lombok.

## Nota

Para que funcione `log.debug` hace falta indicar en el fichero `application.properties` la siguiente property: `logging.level.com.jmunoz=debug`

## Testing

- Clonar el repositorio
- Ir a Maven y seleccionar del Lifecycle la opción Compile
  - Deberíamos ver en la carpeta `target/classes/com/jmunoz/restmvc/model/Beer.class` los getter, setter, el constructor sin argumentos, equals() y hashMap(), el método toString() y el Builder
  - Deberíamos ver en la carpeta `target/classes/com/jmunoz/restmvc/controller/BeerController.class` el constructor generado
- Leer los comentarios de las clases
- Ejecutar el test
  - Deben verse los logs con nivel DEBUG y la instancia de Beer 