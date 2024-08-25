# spring-6-resource-server

Usando las credenciales de OAuth2 generadas en el proyecto anterior `spring-6-auth-server`, vamos a dar seguridad a nuestro RESTful API

Partimos del proyecto `spring-6-security-basic` y vamos a usar Spring Security para configurarlo como un OAuth2 Resource Server, es decir, que va a aceptar un Token JWT que obtendremos del Authentication Server y usarlo para dar seguridad a nuestras APIs

## Notas

1. Necesitamos añadir al POM la dependencia de resource-server

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql`
  - Importante si queremos usar MySql en vez de H2
- Ejecutar también el proyecto `spring-6-auth-server`
  - Obtener un token usando el endpoint que está en su carpeta `postman`, y, una vez obtenido el token, pulsar `Use Token`
  - Ejecutar el endpoint GET pulsando el botón `Send`
  - Debe aparecer todo el listado de cervezas
  - Si eliminamos algún carácter del token, veremos que da error 401, Unauthorized