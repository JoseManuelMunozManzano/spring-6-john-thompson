# spring-6-webflux-rest-exception-handling

Continuamos con el ejemplo `spring-6-webflux-rest` añadiendo manejo de excepciones, tanto al proyecto como a sus tests.

En contextos reactivos, las excepciones son eventos que tenemos que manejar. En caso contrario, si la excepción empieza a ir hacia arriba, puede parar toda la pila reactiva.

## Notas

1. Lanzar excepción en un contexto reactivo

Este es un ejemplo de `BeerController.java`

```
return beerService.getBeerById(beerId)
    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));    
```

Si se devuelve vacío entonces lanzamos una excepción ResponseStatusException que nos permite indicar el status HTTP.

Esto se maneja en el test `BeerControllerTest.java` de la siguiente forma:

```
webTestClient.get().uri(BeerController.BEER_PATH_ID, 999)
        .exchange()
        .expectStatus().isNotFound();
```

Indicando que el status esperado es 404 (Not Found)

## Testing

- Clonar el repositorio
- En la carpeta `resources` existe el archivo `schema.sql` que se ejecuta automáticamente al arrancar el proyecto, gracias a la clase de configuración `DatabaseConfig.java`
- Ejecutar el proyecto
- En la carpeta `postman` se encuentra un archivo que hay que importar a Postman e ir ejecutando
- Ejecutar el testing a los controladores, que son `BeerControllerTest.java` y `CustomerControllerTest.java`, y es donde está la gestión de excepciones
