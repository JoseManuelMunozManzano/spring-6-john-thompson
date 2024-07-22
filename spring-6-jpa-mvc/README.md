# spring-6-jpa-mvc

Es el mismo ejemplo `spring-6-rest-mvc` pero usando Spring Data JPA y trabajando con la BBDD H2.

## Notas

Se han refactorizado las clases del modelo para que su nombre acabe en `Dto`, dejando claro que son Data Transfer Object.

Importante es ver como en las clases Entities se usan anotaciones (ver @Column) para que Hibernate cree los campos y la tabla de BBDD para la BBDD en memoria H2.

## Testing

- Clonar el repositorio
- Ejecutar los tests