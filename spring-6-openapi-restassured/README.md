# spring-6-openapi-restassured

Continuamos desde el proyecto `spring-6-openapi`.

RestAssured es una biblioteca de testing muy popular para RESTful APIs. Vamos a hacer tests.

Usando swagger-request-validator (ver documentación), creado por Atlassian (los de Jira), vamos a validar que nuestras peticiones casan con nuestra especificación OpenAPI.

Ver documentación: `https://bitbucket.org/atlassian/swagger-request-validator/src/master/`

## Notas

1. Necesitamos añadir al POM las dependencias de RestAssured y Swagger Request Validator para RestAssured

```
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>com.atlassian.oai</groupId>
    <artifactId>swagger-request-validator-restassured</artifactId>
    <version>2.40.0</version>
</dependency>
```

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql`
  - Importante si queremos usar MySql en vez de H2
- Lo importante aquí son los tests. En concreto:
  - En `controller/BeerControllerRestAssuredTest.java`
- Para trabajar con Swagger Request Validator necesitamos tener el archivo `oa3.yml` (OpenAPI Specificacion) en la carpeta `target`
  - Para obtenerlo, ejecutar el lifecycle de Maven `verify`
- Como nuestros tests necesitan utilizar ese fichero, lo copiamos a la carpeta `test/resources`
  - Hay que crear la carpeta `resources` dentro de la carpeta `test`
- No hace falta ejecutar ningún otro proyecto para ejecutar estos tests