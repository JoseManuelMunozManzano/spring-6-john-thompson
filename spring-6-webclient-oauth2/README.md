# spring-6-webclient-oauth2

Vamos a añadir OAuth2 al proyecto `spring-6-webclient` para convertirlo en un cliente con OAuth2

## Notas

1. Añadir al POM la dependencia de OAuth2, para ser un cliente

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

## Testing

- Clonar el repositorio
- Ejecutar el proyecto `spring-6-webflux-fn` que será nuestro backend
- Ejecutar el test `BeerClientImplTest`
- No ejecutar este proyecto, porque se ejecuta por el mismo puerto que el backend y no va a funcionar. Solo los tests