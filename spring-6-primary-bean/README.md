# spring-6-primary-bean

Ejemplo de app donde usamos la anotación @Primary para que Spring sepa cual de las implementaciones del service debe inyectar por defecto.

También vemos ejemplos del uso de la anotación @Qualifier. Es lo que se usa para tener un control total de lo que queremos que inyecte Spring. Veremos como darle un nombre concreto al bean de Spring y como usar la anotación en inyección por property, por setter y por constructor.

También vamos a ver ejemplos usando Profiles, que son una forma de controlar que beans se conectan al contexto y cuales no.

## Testing

- Clonar el repositorio
- Ejecutar el test `MyControllerTest.java`
  - De la clase `MyController.java` quitar la parte del @Qualifier() y ejecutar de nuevo el mismo test
- Ejecutar el test `PropertyInjectedControllerTest.java`
- Ejecutar el test `SetterInjectedControllerTest.java`
- Profiles. Ejecutar el test `Myi18NControllerTest.java`
