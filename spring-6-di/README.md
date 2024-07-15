# spring-6-di

Vamos a construir una pequeña aplicación web usando Spring Boot para enseñar la inyección de dependencias.

## Qué se usa en la app

- Spring context
  - ApplicationContext ctx = SpringApplication.run(DiApplication.class, args);
  - MyController controller = ctx.getBean(MyController.class);
- Spring context en contextos de test
  - @Autowired ApplicationContext applicationContext;

## Testing

- Clonar el repositorio
- App
  - Ejecutar la aplicación
- Tests
  - Ejecutar los dos tests de la aplicación de manera independiente
    - testAutowireOfController()
    - testGetControllerFromCtx()

Notar que el programa se ejecuta y termina porque no hay servidor Tomcat interno en el POM.
