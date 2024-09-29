# spring-6-webflux-fn

Es continuación del proyecto `spring-6-reactive-mongo`, pero no tiene mucho que ver con lo que ya hay.

Tras haber estado trabajando con Spring WebFlux, que sigue el patrón Web MVC, más o menos el mismo tipo de codificación que Spring MVC, pero de manera reactiva, vamos a ver WebFlux fn, que es completamente diferente. Usa un modelo de programación funcional.

`https://docs.spring.io/spring-framework/reference/web/webflux-functional.html`

La idea es básicamente configurar `routers` y luego asignar `handlers`, donde los router son dependientes de los handlers.

Tal y como puede verse en la url de arriba:

```
PersonRepository repository = ...
PersonHandler handler = new PersonHandler(repository);

RouterFunction<ServerResponse> route = route()
	.GET("/person/{id}", accept(APPLICATION_JSON), handler::getPerson)
	.GET("/person", accept(APPLICATION_JSON), handler::listPeople)
	.POST("/person", handler::createPerson)
	.build();


public class PersonHandler {

	// ...

	public Mono<ServerResponse> listPeople(ServerRequest request) {
		// ...
	}

	public Mono<ServerResponse> createPerson(ServerRequest request) {
		// ...
	}

	public Mono<ServerResponse> getPerson(ServerRequest request) {
		// ...
	}
}
```

Puede ser un poco difícil acostumbrarse a esta forma de programar, pero es muy eficiente.

Así que, dado un entorno reactivo, se usa programación funcional.

Vamos a realizar los mismos endpoints que hicimos en Spring Web MVC y Spring WebFlux, pero ahora en Spring WebFlux fn.

## Notas

## Testing

- Clonar el repositorio
- Renombrar `application.template.properties` a `application.properties` e indicar sus valores
- Centrarnos en el package `web.fn` y en los tests
- Ejecutar (no hace falta ejecutarlo para los tests) el programa para probar en Postman
  - Se pueden encontrar los endpoints de Postman en la carpeta `postman`