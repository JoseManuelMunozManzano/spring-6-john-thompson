# spring-6-openapi

Vamos a añadir al proyecto `spring-6-resource-server` la generación de OpenApi.

Como ya tenemos el código, usaremos Code First (aunque Specificacion First es preferible)

Vamos a obtener una página Swagger del tipo: `http://server:port/context-path/swagger-ui/index.html`

Vamos a obtener la descripción OpenAPI en una ruta tipo: `http://server:port/context-path/v3/api-docs`

Ver documentación: `https://springdoc.org/#getting-started`

## Notas

1. Necesitamos añadir al POM las dependencias de OpenApi

```
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>

<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-common</artifactId>
    <version>2.3.0</version>
</dependency>
```

2. Necesitamos no securizar los siguientes endpoints en `SpringSecConfig.java`

```
.requestMatchers("/v3/api-docs**", "/swagger-ui/**", "/swagger-ui.html")
.permitAll()
```

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql`
  - Importante si queremos usar MySql en vez de H2
- En el navegador ejecutar las rutas:
  - `http://localhost:8080/v3/api-docs`
    - Este es el JSON body de la especificación OpenAPI
  - `http://localhost:8080/v3/api-docs.yaml`
    - Esta es la descarga del JSON body de la especificación OpenAPI
  - `http://localhost:8080/swagger-ui/index.html`
    - Esta es la página Swagger