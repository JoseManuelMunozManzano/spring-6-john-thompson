# spring-6-di-no-spring

Ejemplo de web app donde hay dependencias y se inyectan, pero sin usar Spring. La inyección es manual.

Se exploran inyecciones usando la property (menos preferida), setter y constructor (el preferido)

## Testing

- Clonar el repositorio
- Ejecutar los tests, método sayHello():
  - `PropertyInjectedControllerTest.java`
  - `SetterInjectedControllerTest.java`
  - `ConstructorInjectedControllerTest.java`

Notar que el programa se ejecuta y termina porque no hay servidor Tomcat interno en el POM.
