# spring-6-jpa-mvc

Es el mismo ejemplo `spring-6-rest-mvc` pero usando Spring Data JPA y trabajando con la BBDD H2.

## Notas

Se han refactorizado las clases del modelo para que su nombre acabe en `Dto`, dejando claro que son Data Transfer Object.

Importante es ver como en las clases Entities se usan anotaciones (ver @Column) para que Hibernate cree los campos y la tabla de BBDD para la BBDD en memoria H2.

También se ve como se configura MapStruct en el fichero `pom.xml`.

Una vez creados los mappers, se puede ir al ciclo de vida de `Maven` y ejecutar los pasos `clean` y `compile`. En la carpeta `target` se pueden ver las implementaciones de los mappers.

Las funciones lambda no actualizan ninguna variable local, ya que son effective final o final. Cuando queremos actualizar un valor dentro de una función lambda, se usa `AtomicReference`. Ver `BeerServiceJPA`.

Esto se hace para devolver el objeto actualizado, o la excepción `NotFoundException`.

## Testing

- Clonar el repositorio
- Ejecutar los tests
  - Se testean varias cosas, como la carga de datos en `BootstrapDataTest`, y la interacción entre el service y el controller, es decir, tests de integración usando la BBDD H2, en `BeerControllerIT` y `CustomerControllerIT`.