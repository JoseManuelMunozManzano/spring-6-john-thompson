# spring-6-resttemplate-security-basic

Es el mismo proyecto `spring-6-resttemplate` al que se le ha añadido Http Basic Security.

## Notas

## Testing

- Clonar el repositorio
- Renombrar `application.template.properties` a `application.properties` e indicar sus valores
- Ejecutar el proyecto `spring-6-security-basic` que será nuestro programa de backend que tiene seguridad
  - Se ejecuta en el puerto 8080
- Ejecutar los tests de este proyecto, que será el cliente y llamará a los endpoints del proyecto backend
  - Mirar el test `BeerClientImplTest`