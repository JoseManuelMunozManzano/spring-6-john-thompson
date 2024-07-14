# spring-6-webapp

Vamos a construir una pequeña aplicación web usando Spring Boot para demostrar lo rápido que pueden crearse.

## Qué se usa en la app

- Capa domain
  - Mapeo JPA
  - Anotaciones
    - @Entity
    - @Id
    - @GeneratedValue(strategy = GenerationType.AUTO)
  - Relaciones entre entities
    - @ManyToMany
    - @ManyToMany(mappedBy = "authors")
    - @JoinTable(name = "author_book", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
  - equals() y hashCode()
  - toString()
- Capa repositories
  - interface que extiende de CrudRepository<Author, Long>

## Links

https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html

## Testing

- Clonar el repositorio
- Ejecutar la aplicación
