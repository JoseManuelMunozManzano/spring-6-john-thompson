# spring-6-di-spring

Ejemplo de web app donde hay dependencias y se inyectan usando Spring.

Se exploran inyecciones usando la property (menos preferida), setter y constructor (el preferido)

Lo primero que tenemos que hacer es anotar la clase service que implementa la interface con el stereotype @Service.

Igualmente anotamos las tres clases controller con el stereotype @Controller.

De este forma, indicamos a Spring que tantos las clases controller como la implementación del service son componentes de Spring.

Mirar también las clases de Testing.

## Testing

- Clonar el repositorio
- Ejecutar los tests:
  - `PropertyInjectedControllerTest.java`
  - `SetterInjectedControllerTest.java`
  - `ConstructorInjectedControllerTest.java`

Notar que el programa se ejecuta y termina porque no hay servidor Tomcat interno en el POM.
