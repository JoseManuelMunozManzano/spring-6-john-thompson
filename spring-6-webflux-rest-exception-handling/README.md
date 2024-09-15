# spring-6-webflux-rest-exception-handling

Continuamos con el ejemplo `spring-6-webflux-rest` añadiendo manejo de excepciones, tanto al proyecto como a sus tests.

En contextos reactivos, las excepciones son eventos que tenemos que manejar. En caso contrario, si la excepción empieza a ir hacia arriba, puede parar toda la pila reactiva.

## Notas

## Testing

- Clonar el repositorio
- En la carpeta `resources` existe el archivo `schema.sql` que se ejecuta automáticamente al arrancar el proyecto, gracias a la clase de configuración `DatabaseConfig.java`
- Ejecutar el proyecto
- En la carpeta `postman` se encuentra un archivo que hay que importar a Postman e ir ejecutando
- Ejecutar el testing a los controladores, que son `BeerControllerTest.java` y `CustomerControllerTest.java`
