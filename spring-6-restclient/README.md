# spring-6-restclient

Cogemos toda la estructura del proyecto `spring-6-resttemplate-oauth2` para realizar este proyecto.

RestClient sigue el estilo de programación de WebClient, pero es de naturaleza bloqueante, y realmente está construido sobre RestTemplate, a tal nivel que se puede instanciar un RestClient usando una instancia de RestTemplate.

Con RestClient conseguimos una API diferente, más parecida a la de WebClient.

Para no crear otro proyecto, porque es muy sencillo, añadimos Logbook aquí mismo.

## Notas

1. Hemos cogido un proyecto ya construido sobre RestTemplate.

Los cambios hechos para que trabaje como RestClient son:

- En `config/RestTemplateBuilderConfig` se ha creado el método `restClientBuilder()`
  - Vemos que depende de RestTemplateBuilder(), que se instancia primero
- En `client/BeerClientImpl` todo lo implementado ya es nuevo para RestClient
- En `test/client/BeerClientMockTest` se ha añadido la variable `RestClient.Builder restClientBuilder` y modificado el método `setUp()`

2. Logbook

Para trabajar con Logbook solo hay que añadir la dependencia

```
<properties>
    <logbook.version>3.9.0</logbook.version>
</properties>

<dependency>
    <groupId>org.zalando</groupId>
    <artifactId>logbook-spring</artifactId>
    <version>${logbook.version}</version>
</dependency>
```

Hay que realizar configuración, en el package `config`, fuente `RestTemplateBuilderConfig.java` para añadir un interceptor.

Y la siguiente property en application.properties: `logging.level.org.zalando.logbook=trace`.

En clientes, es muy útil para testear lo que se pasa a través de la red, para saber lo que hace nuestro cliente.

## Testing

- Clonar el repositorio
- Renombrar `application.template.properties` a `application.properties` e indicar sus valores
- Ejecutar los tests de la clase `BeerClientImplTest.java`
    - Tienen que estar en ejecución los siguientes proyectos: `spring-6-auth-server`, `spring-6-gateway` y `spring-6-resource-server`, que es el MVC
- Ejecutar los tests de la clase `BeerClientMockTest.java`
    - Para estos tests no es necesario que se ejecute ningún otro proyecto
- Para probar `Logbook` vamos a ejecutar el test `BeerClientMockTest`, método `testListBeersWithQueryParam()`
  - Como es un mock, no hace falta ejecutar ningún otro proyecto 
  - Debe verse la traza de la petición y la respuesta