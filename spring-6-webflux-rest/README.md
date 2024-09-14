# spring-6-webflux-rest

Continuamos el ejemplo `spring-6-data-r2dbc`. Una vez tenemos persistencia hecha de manera reactiva, vamos a hacer la funcionalidad web.
    
Spring WebFlux MVC tiene casi las mismas anotaciones que encontramos en Spring MVC. Esto se ha hecho intencionalmente para que los desarrolladores tengan una transición más suave a la pila reactiva.

Vamos a crear controladores reactivos, mapeos con MapStruct, Services, usar Path Variables, validaciones...

Añadimos el punto de testing a los controladores usando WebTestClient. Usando controladores reactivos no podemos usar Spring Mock MVC, porque este está muy ligado a Servlet API, que es bloqueante. WebFlux Test Client no es bloqueante.

El truco es: excepciones y programación reactiva son eventos que necesitamos escuchar y gestionar.

## Notas

1. Netty

Con Spring WebFlux, se ejecuta el web server Netty. Recordar que con Spring MVC se ejecutaba el web server Tomcat.

2. MapStruct

Se ve como se configura MapStruct en el fichero `pom.xml`. No olvidar refrescar Maven.

Una vez creados los mappers, se puede ir al ciclo de vida de `Maven` y ejecutar los pasos `clean` y `compile`. En la carpeta `target` se pueden ver las implementaciones de los mappers.

3. ResponseEntity<Void>

Es la forma de declarar una respuesta vacía.

4. Truco para generar JSON que luego puede usarse en Postman

```
// El objetivo de este método es servir de utilidad para generar JSON
// y poder hacer testing de endpoints WebFlux.
// TIP: Ejecutamos este test, cogemos el resultado y ya tenemos un JSON para hacer pruebas en Postman.
@Test
void testCreateJson() throws JsonProcessingException {
    // Normalmente el contexto de Spring Boot hace el autowired de una instancia
    // preconfigurada de Jackson, porque usamos el test slice.
    // Pero no va a estar en el contexto y por eso añadimos un ObjectMapper.
    ObjectMapper objectMapper = new ObjectMapper();

    System.out.println(objectMapper.writeValueAsString(getTestBeer()));
}

// Helper method para crear una objeto Beer
Beer getTestBeer() {
    return Beer.builder()
            .beerName("Space Dust")
            .beerStyle("IPA")
            .price(BigDecimal.TEN)
            .quantityOnHand(12)
            .upc("123213")
            .build();
}
```

5. Cuando usar o no .subscribe()

Whenever you have a Mono or Flux object, you must explicitly request for the data it returns, and this can be done using the subscribe().
When you do this, you are applying back pressure and telling the publisher "I am ready, please send me the data".
But when you are talking about a method from an endpoint which returns a Mono or a Flux, this "request for data" is implicit in the HTTP request itself since it is, by concept, a request.

Difference between return a Mono<ResponseEntity<Void>>, or just return the ResponseEntity<Void>.
- If you do the first one, you don't need to subscribe, because the request for data is implicit on the HTTP request sent by the client (in this case, postman).
- If you do the second one, you need to request for the data on the controller, hence the subscribe().


## Testing

- Clonar el repositorio
- En la carpeta `resources` existe el archivo `schema.sql` que se ejecuta automáticamente al arrancar el proyecto, gracias a la clase de configuración `DatabaseConfig.java`
- Ejecutar el proyecto
- En la carpeta `postman` se encuentra un archivo que hay que importar a Postman e ir ejecutando
- Ejecutar el testing a los controladores, que son `BeerControllerTest.java`