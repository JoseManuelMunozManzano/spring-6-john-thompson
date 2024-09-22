# spring-6-reactive-mongo

Nuevo proyecto en el que se prueba la conectividad de Spring 6 con la BBDD no relacional MongoDB usando programación reactiva.

## Notas

1. Awaitility

Es una utilidad Java que nos sirve para realizar tests, y la usamos cuando el test termina antes de que se complete
un subscribe.

Hay que añadir esta dependencia al POM

```
<dependency>
    <groupId>org.awaitility</groupId>
    <artifactId>awaitility</artifactId>
    <version>4.2.1</version>
    <scope>test</scope>
</dependency>
```

Hay otro framework muy parecido en cuanto a funcionamiento llamado `StepVerifier`. Las diferencias serían:

One key difference between these two frameworks is that Awaitility is more focused on general asynchronous testing, while StepVerifier is specifically designed for testing reactive streams.

Another difference is that Awaitility relies on polling to check for completion of asynchronous operations, whereas StepVerifier is more deterministic, as it simulates events in the stream and waits for the expected results.

2. Query Methods usando MongoDB

`https://docs.spring.io/spring-data/mongodb/reference/repositories/query-methods-details.html#repositories.query-methods.query-creation`

3. Diferencia entre findBy... y findFirst...

Si se encuentra más de un registro, findBy lanza una excepción, mientras que findFirst devuelve el primero que pilla.

## Testing

- Clonar el repositorio
- Renombrar `application.template.properties` a `application.properties` e indicar sus valores
- Ejecutar el test `BeerServiceImplTest.java`