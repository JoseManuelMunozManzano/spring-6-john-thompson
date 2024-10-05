# spring-6-openapi

Vamos a añadir al proyecto `spring-6-resource-server` la generación de OpenApi.

Como ya tenemos el código, usaremos Code First (aunque Specificacion First es preferible)

Vamos a obtener una página Swagger del tipo: `http://server:port/context-path/swagger-ui.html`

Vamos a obtener la descripción OpenAPI en una ruta tipo: `http://server:port/context-path/v3/api-docs`

Ver documentación: `https://springdoc.org/#getting-started`

## Notas

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql`
  - Importante si queremos usar MySql en vez de H2