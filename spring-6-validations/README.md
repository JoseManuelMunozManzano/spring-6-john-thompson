# spring-6-validations

Es el mismo ejemplo `spring-6-rest-mvc` pero usando validaciones a la data de la request para asegurar la integridad de la misma.

Vamos a usar la API Java Bean Validation (cambiado luego su nombre a Jakarta Bean Validation)

También vamos a usar JPA Validation, es decir, poner las mismas validaciones de los DTO's en los Entities.

Se hacen tests, dentro de los tests de integración, a la capa JPA.

## Notas

`@Valid` is a standard Java Bean Validation API annotation while `@Validated` is a Spring-specific annotation.

`@Valid` is used primarily for method parameters and fields while `@Validated` can be used at the class level or method level and supports group validation (allows different validation rules to be applied based on the validation group specified).

So, for basic, straightforward validation needs, use `@Valid`. For more advanced validation features, such as group validation, or when you want to apply validation at the class level, go for `@Validated`.

## Testing

- Clonar el repositorio
- Ejecutar los tests