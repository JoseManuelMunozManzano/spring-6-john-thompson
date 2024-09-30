# spring-6-webflux-resource-server

Vamos a coger el servidor de Spring reactivo `spring-6-webflux-rest-exception-handling` y se va a añadir OAuth, haciéndolo un OAuth Resource Server para realizar tareas de autorización

## Notas

1. Añadir al POM la dependencia de OAuth2

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

## Testing

- Clonar el repositorio
- En la carpeta `resources` existe el archivo `schema.sql` que se ejecuta automáticamente al arrancar el proyecto, gracias a la clase de configuración `DatabaseConfig.java`
- Ejecutar el proyecto
- En la carpeta `postman` se encuentra un archivo que hay que importar a Postman e ir ejecutando