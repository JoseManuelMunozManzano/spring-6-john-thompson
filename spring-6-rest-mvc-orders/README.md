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

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql`