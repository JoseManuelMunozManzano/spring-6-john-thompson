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
    - @OneToMany(mappedBy = "publisher")
    - @ManyToOne
  - equals() y hashCode()
  - toString()
- Capa repositories
  - interface que extiende de CrudRepository<Author, Long>
- Capa bootstrap para inicialización de datos durante la ejecución
  - implements CommandLineRunner
  - Anotaciones
    - @Component
- Capa BBDD
  - Vamos a habilitar la consola H2
    - En application.properties incluimos
      - spring.h2.console.enabled=true
- Capa services
  - @Service
- Capa controllers
  - @Controller
  - @RequestMapping("/books")
  - Model
    - model.addAttribute("books", bookService.findAll());
- Capa vista, creada con plantillas Thymeleaf en la carpeta resources/templates

## Links

https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html

## Testing

- Clonar el repositorio
- Ejecutar la aplicación
- Ver la consola H2
  - Coger los datos de la consola de ejecución de la aplicación
  - La URL será: `http://localhost:8080/h2-console`
  - JDBC URL cambia con cada ejecución de la aplicación
  - Usuario es `sa` y la contraseña dejarla a blancos
- Acceder a la ruta: `http://localhost:8080/books` para obtener todos los libros
- Acceder a la ruta: `http://localhost:8080/authors` para obtener todos los autores
