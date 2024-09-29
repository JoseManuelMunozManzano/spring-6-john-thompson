# spring-6-webclient

WebClient es la tecnología que quiere sustituir a RestTemplate. Usa tecnología reactiva.

Al igual que RestTemplate, con WebClient podemos hacer llamadas a un back, es decir, WebClient nos sirve de cliente.

## Notas

1. Para usar `awaitility` hay que añadir al POM la siguiente dependencia

```
<dependency>
    <groupId>org.awaitility</groupId>
    <artifactId>awaitility</artifactId>
    <version>4.2.2</version>
</dependency>
```

2. flatMap vs map

The .flatMap() operator will create an inner Mono, which allows you to perform further operations on the result of the mapping.

With map(), the result of the mapping is returned as-is and cannot be further operated on.

3. body(Mono.just(beerDto), BeerDTO.class) vs bodyValue(beerDto)

In Spring WebFlux, when working with the ServerResponse class, you can use the body and bodyValue methods to set the response body.

Let's understand the difference between body(Mono.just(beerDto), BeerDTO.class) and bodyValue(beerDto) methods:

- body(Mono.just(beerDto), BeerDTO.class): This method is used to set the response body as a Mono instance that emits the specified value.
It also allows you to provide the type of the value for serialization purposes. Here's an example:

```
Mono<ServerResponse> responseMono = ServerResponse.ok()
    .body(Mono.just(beerDto), BeerDTO.class);
```

In this example, Mono.just(beerDto) represents a Mono that emits the beerDto object as the response body.
  
The BeerDTO.class parameter is used for serialization, specifying the type of the object being emitted.

The body method with a Mono allows you to handle asynchronous scenarios, where the response body might be generated asynchronously or fetched from a reactive data source.

- bodyValue(beerDto): This method is used to set the response body directly with the specified value.
It doesn't require a Mono wrapper and is typically used for simple values or when you have already prepared the response body.
Here's an example:

```
Mono<ServerResponse> responseMono = ServerResponse.ok()
    .bodyValue(beerDto);
```

In this example, beerDto is directly passed as the response body value.

The bodyValue method automatically handles serialization of the value based on the content type set in the response.


Use body(Mono.just(value), Type.class) when you need to handle asynchronous scenarios or when you want to provide explicit type information for serialization.

Use bodyValue(value) when you have a simple value or when the response body is already prepared.

Both methods allow you to set the response body in a reactive manner using Spring WebFlux.

## Testing

- Clonar el repositorio
- Ejecutar el proyecto `spring-6-webflux-fn` que será nuestro back
- Ejecutar el test `BeerClientImplTest`