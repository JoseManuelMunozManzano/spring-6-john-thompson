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

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores