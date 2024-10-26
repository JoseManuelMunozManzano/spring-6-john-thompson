# spring-6-rest-mvc-orders

Se va a partir del proyecto MVC (`spring-6-rest-mvc-events`) y se van a añadir endpoints para la funcionalidad de Orders.

El objetivo de este proyecto es hacer cada parte como ejercicio, para reforzar los conocimientos adquiridos.

- Crear DTOs
- Crear Mappers
- Añadir Validaciones
- TDD antes de realizar la funcionalidad (primero get, luego create, update, patch y delete)
- Implementar la funcionalidad (primero get, luego create, update, patch y delete)
- Por último, añadiremos auditoría para las operaciones de Order

## Notas

1. Creamos DTOs para las entidades

BeerOrderEntity
BeerOrderLine
BeerOrderShipment

2. Creamos Mappers para

BeerOrderEntity -a/desde Dto
BeerOrderLineEntity -a/desde Dto
BeerOrderShipmentEntity -a/desde Dto

Al ejecutar Maven Compile, se crearán automáticamente las clases.

3. Añadir Validaciones

Hay que añadir validaciones tanto a DTOs como a Entities.

4. Inicializar Beer Order Data

Para propósitos de testing, deberíamos cargar data inicial (bootstrap data).

A cada uno de los Customers le vamos a crear dos orders con dos beers.

5. Añadir TDD para operaciones GET

Usaremos Spring Mock MVC para testear List Beer Orders y Get Beer Order by Id.

Crearemos un controller con URLs como constantes para usarlas en los tests.

6. Implementamos las operaciones GET

Creamos `BeerOrderService` con los métodos `GetById` y `ListBeerOrders`, en este último usando paginación (número de página a 1 y tamaño de página a 25 por defecto).

Implementamos los métodos del controller.

Verificar que se pasan los tests.

7. Añadir DTO para Operación Create

Los DTO existentes (BeerOrderDto y BeerOrderLineDto) representan la estructura de datos de la entidad y las relaciones de objetos.

Esto NO es apropiado para las operaciones de creación y actualización.

Para las relaciones, solo se necesita una referencia a la propiedad de ID. Es decir, no estamos creando o actualizando entidades de customers o de beer.

Crear un DTO con:
  - customer id, y beer id como referencias

Esto lo necesitamos para encontrar el customer y el beer que vamos a asociar con la BeerOrderLine.

La BeerOrderShipment no es necesaria en la operación de creación.

Creamos, en el package `model`, las clases `BeerOrderCreateDto` y `BeerOrderLineCreateDto`.

8. Añadir TDD para operación CREATE

Usaremos Spring Mock MVC para testear Create BeerOrder.

Verificaremos el header en busca de la property Location devuelta.

9. Implementamos la operación CREATE

El método en el controlador espera un `BeerOrderCreateDto` y devuelve `201` con la property `location` en el header.

Añadir método en el service para persistir una `BeerOrder` y devolver la entidad grabada al controller.

Devolver `404 Not found` si algún id no se encuentra.

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql`
  - Esto para la parte de ver que se ha añadido la data en el package `bootstrap`
- Para el TDD, lo que hay que ejecutar son los tests, no el proyecto
  - `controller/BeerOrderControllerIT`