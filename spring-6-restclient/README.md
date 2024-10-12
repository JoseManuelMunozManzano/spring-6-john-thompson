# spring-6-restclient

Cogemos toda la estructura del proyecto `spring-6-resttemplate-oauth2` para realizar este proyecto.

RestClient sigue el estilo de programación de WebClient, pero es de naturaleza bloqueante, y realmente está construido sobre RestTemplate, a tal nivel que se puede instanciar un RestClient usando una instancia de RestTemplate.

Con RestClient conseguimos una API diferente, más parecida a la de WebClient.

## Notas

1. Hemos cogido un proyecto ya construido sobre RestTemplate.

Los cambios hechos para que trabaje como RestClient son:

- En `config/RestTemplateBuilderConfig` se ha creado el método `restClientBuilder()`
  - Vemos que depende de RestTemplateBuilder(), que se instancia primero
- En `client/BeerClientImpl` todo lo implementado ya es nuevo para RestClient
- En `test/client/BeerClientMockTest` se ha añadido la variable `RestClient.Builder restClientBuilder` y modificado el método `setUp()`

## Testing

- Clonar el repositorio
- Renombrar `application.template.properties` a `application.properties` e indicar sus valores
- Ejecutar los tests de la clase `BeerClientImplTest.java`
    - Tienen que estar en ejecución los siguientes proyectos: `spring-6-auth-server`, `spring-6-gateway` y `spring-6-resource-server`, que es el MVC
- Ejecutar los tests de la clase `BeerClientMockTest.java`
    - Para estos tests no es necesario que se ejecute ningún otro proyecto