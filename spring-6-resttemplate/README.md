# spring-6-resttemplate

Proyecto nuevo con un ejemplo de uso de RestTemplate para hacer de cliente y realizar llamadas a endpoints.

Indicar que ahora se usa más WebClient como Rest Client reactivo. Lo veremos más adelante en el curso.

## Notas

1. Vemos pruebas de uso de Jackson para hacer parseo de JSON usando:

  ResponseEntity<String>

  ResponseEntity<Map>

  ResponseEntity<JsonNode>

  ResponseEntity<mi_pojo_java>

2. Vemos como mapear de un JSON a una clase POJO usando anotaciones

Ver `BeerDTOPageImpl.java`.

![alt Jackson to Pojo](../images/12-Jackson-to-POJO.png)

## Testing

- Clonar el repositorio
- Ejecutar el proyecto `spring-6-db-relationships` que será nuestro programa de backend
  - Se ejecuta en el puerto 8080
  - Nos concentramos en el método listBeers()
- Ejecutar los tests de este proyecto, que será el cliente y llamará a los endpoints del proyecto backend
  - Mirar el test `BeerClientImplTest`
  - No ejecutar el proyecto, solo el test, ya que fallará indicando que el puerto 8080 ya se está usando