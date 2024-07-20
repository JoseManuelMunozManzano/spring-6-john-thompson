# spring-6-rest-mvc

Vamos a hacer un ejemplo de app REST MVC.

## Notas

Para añadir Live Reload al proyecto hay que añadir al POM la dependencia `DevTools`:

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
</dependency>
```

Y para que funcione en IntelliJ hay que habilitar lo siguiente en Settings:

![alt DevTools](../images/03-DevTools.png)


Las buenas prácticas al trabajar con RESTful API indican devolver al cliente información sobre el objeto que se creó y la propiedad del header llamada `location` 

```
@PostMapping
public ResponseEntity<Beer> handlePost(@RequestBody Beer beer) {
    Beer savedBeer = beerService.saveNewBeer(beer);

    // Una buena práctica al trabajar con RESTful APIs es devolver al cliente
    // información sobre el objeto que se creó y la propiedad del header llamada location.

    // Esto va al header
    // La idea es tener un URL para acceder a ese id en concreto.
    // Es decir, podemos copiar, de los headers de la respuesta al hacer el POST, esa URL
    // e ir al endpoint Get Beer By Id y pegar ese URL.
    // Es como una ayuda a la hora de obtener el id.
    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedBeer.getId())
            .toUri();

    // Aquí devolvemos al cliente tanto la propiedad location como el registro creado
    return ResponseEntity.created(location).body(savedBeer);
}
```

Otra forma, más fea, de hacer lo mismo es esta:

```
@PostMapping
public ResponseEntity<Customer> handlePost(@RequestBody Customer customer) {
    Customer savedCustomer = customerService.saveNewCustomer(customer);

    // Otra forma de crear la property location en los headers.
    // Es más bonita la forma mostrada en BeerController.
    HttpHeaders headers = new HttpHeaders();
    headers.add("Location", "/api/v1/customer/" + savedCustomer.getId().toString());

    return new ResponseEntity<>(headers, HttpStatus.CREATED);
}
```

## Testing

- Clonar el repositorio
- Ejecutar el proyecto
- Importar la carpeta postman a Postman y probar los endpoints
  - Get All Beers
  - Coger uno de los Id y probar Get Beer By Id
  - Get All Customers
  - Coger uno de los Id y probar Get Customer By Id
  - Add New Beer
    - Ir a los headers de la respuesta, a la key location, y ver que tenemos la URL
  - Add New Customer
    - Ir a los headers de la respuesta, a la key location, y ver que tenemos la URL
  - Update Beer usando un Id y un body
  - Update Customer usando un Id y un body
  - Delete Beer usando un Id
  - Delete Customer usando un Id
  - Patch Beer usando un Id y un body