# Spring Framework 6: Beginner to Guru

- Construyendo una Web App con Spring Boot
  - spring-6-webapp
    - Vamos a construir una pequeña aplicación web usando Spring Boot
- Inyección de Dependencias con Spring
  - spring-6-di
    - Ejemplo para enseñar inyección de dependencias
  - spring-6-no-di
    - Ejemplo de dependencias sin inyección de dependencias
  - spring-6-di-no-spring
    - Ejemplo de dependencias con inyección de dependencias pero que no usa Spring
  - spring-6-di-spring
    - Ejemplo de inyección de dependencias usando Spring
  - spring-6-primary-bean
    - Ejemplo usando la anotación @Primary
    - Ejemplo usando la anotación @Qualifier
    - Ejemplo usando profiles
  - spring-6-di-assignment
    - Ejercicio de inyección de dependencias usando perfiles
  - spring-6-life-cycle
    - Ejemplo del ciclo de vida de Spring
- Project Lombok
  - spring-6-lombok
    - Ejemplo con Project Lombok
- Spring MVC Rest Services
  - spring-6-rest-mvc
    - Ejemplo CRUD completo
    - Ejemplo de testing usando Spring MockMVC con Mockito
    - Ejemplo de manejo de excepciones con Spring MVC y uso en tests
- Spring Data JPA with Spring MVC
  - spring-6-jpa-mvc
    - Ejemplo de Spring Data JPA trabajando con la BBDD H2
- Data Validation Framework
  - spring-6-validations
    - Ejemplo de Spring MVC en la que se realizan validaciones a los datos de entrada de la request
- MySQL
  - spring-6-mysql
    - Ejemplo de Spring MVC en la que se utiliza la BBDD MySQL
- Flyway
  - spring-6-flyway
    - Ejemplo de Spring MVC en la que se utiliza la BBDD MySQL y el migrador Flyway
- CSV File Uploads
  - spring-6-csv
    - Ejemplo de Spring MVC en la que se utiliza CSV para subida de archivos. La idea es poblar de data la BD
- Query Parameters with Spring MVC
  - spring-6-query-parameters
    - Ejemplo de uso de Spring MVC que acepta Query Parameters y Spring Data JPA para devolver y consultar la BD
- Paging and Sorting
  - spring-6-paging-sorting
    - Ejemplo de uso de Spring MVC con paginación y ordenamiento
- Database Relationship Mapping
  - spring-6-db-relationships
    - Ejemplo de uso de Spring MVC con mapeos de relaciones entre tablas
    - También hay un par de ejemplos de helper methods para ayudar a cargar datos en los campos que estableces relaciones con otras entidades
    - Y, en vez de usar helper methods, también se puede usar persistencia en cascada. Vemos un ejemplo
- Database Transactions & Locking
  - spring-6-db-transactions
    - Ejemplo donde se ve un ejemplo de fallo de bloqueo optimista
- Introduction to Spring Data REST
  - spring-6-data-rest
    - Es un ejemplo de, solo definiendo nuestras entidades y los repositorios, se genera una creación automática de controladores
- Spring RestTemplate
  - spring-6-resttemplate
    - Proyecto nuevo con un ejemplo de uso de RestTemplate para hacer de cliente y realizar llamadas a endpoints del proyecto `spring-6-db-relationships`
    - Se ven muchos ejemplos de uso de Jackson
    - Se ve como se testea Spring RestTemplate usando Mockito
- Spring Security Basic Auth
  - spring-6-security-basic
    - Seguimos con el proyecto `spring-6-db-relationships` con un ejemplo de como securizar nuestras APIs
  - spring-6-resttemplate-security-basic
    - Añadimos seguridad básica al proyecto Rest Template
- Spring Authorization Server
  - spring-6-auth-server
    - Es un nuevo proyecto
    - Como pasos para conseguir autorización con el framework OAuth2 y token de autenticación JWT (en vez de la seguridad básica que ya hemos visto), vamos a:
      - Configurar un server de autenticación usando el proyecto Spring Authentication Server
      - Configurar un resource server
      - Como puede un RestTemplate obtener el token y usarlo para autenticarse
    - En este proyecto vemos la configuración del server de autenticación usando el proyecto Spring Authentication Server
- Spring MVC OAuth2 Resource Server
  - spring-6-resource-server
    - Usando las credenciales de OAuth2 generadas en el proyecto anterior `spring-6-auth-server`, vamos a dar seguridad a nuestro RESTful API
    - Partimos del proyecto `spring-6-security-basic` y vamos a usar Spring Security para configurarlo como un OAuth2 Resource Server, es decir, que va a aceptar un Token JWT que obtendremos del Authentication Server y usarlo para dar seguridad a nuestras APIs
- Spring RestTemplate with OAuth2
  - spring-6-resttemplate-oauth2
    - Evolucionamos el proyecto `spring-6-resttemplate-security-basic` para poder acceder al Authentication Server, obtener un Token JWT, y luego usarlo en una petición del Resource Server
- Introduction to Reactive Programming
  - spring-6-intro-reactive
    - Proyecto nuevo para mostrar las capacidades de la programación reactiva, que se basa en un modelo no bloqueante de programación, e introduciéndonos en la forma es la que se usa en SpringBoot
- Spring Data R2DBC
  - spring-6-data-r2dbc
    - Vamos a ver un proyecto de ejemplo de operaciones de persistencia de datos contra una BD usando el framework Spring Data R2DBC
    - Usa drivers reactivos para BD relacionales
    - Esta tecnología reactiva contra la BD es todavía muy reciente
- Spring WebFlux Rest Services y WebTestClient
  - spring-6-webflux-rest
    - Continuamos el ejemplo `spring-6-data-r2dbc`. Una vez tenemos persistencia hecha de manera reactiva, vamos a hacer la funcionalidad web
    - Añadimos WebTestClient (no bloqueante) al proyecto para hacer el testing de los controllers
- Exception Handling with Spring WebFlux
  - spring-6-webflux-rest-exception-handling
    - Continuamos con el ejemplo `spring-6-webflux-rest` añadiendo manejo de excepciones, tanto al proyecto como a sus tests
- Spring Data MongoDB
  - spring-6-reactive-mongo
    - Nuevo proyecto en el que se prueba la conectividad de Spring 6 con la BBDD no relacional MongoDB usando programación reactiva
- Spring WebFlux.fn Rest
  - spring-6-webflux-fn
    - Es continuación del proyecto `spring-6-reactive-mongo`, pero no tiene mucho que ver con lo que ya hay.
    - Tras haber estado trabajando con Spring WebFlux, que sigue el patrón Web MVC, más o menos el mismo tipo de codificación que Spring Web MVC, pero de manera reactiva, vamos a ver WebFlux fn, que es completamente diferente. Es un modelo de programación funcional
- Spring WebClient
  - spring-6-webclient
    - Proyecto nuevo con un ejemplo de uso de WebClient para hacer de cliente y realizar llamadas a endpoints del proyecto `spring-6-webflux-fn`
    - WebClient es la sustitución de RestTemplate
- Spring WebFlux Resource Server
  - spring-6-webflux-resource-server
    - Vamos a coger el servidor de Spring reactivo `spring-6-webflux-rest-exception-handling` y se va a añadir OAuth, haciéndolo un OAuth Resource Server para realizar tareas de autorización
- Spring WebFlux.fn Resource Sever
  - spring-6-webflux-fn-resource-server
    - Vamos a añadir OAuth2 al proyecto `spring-6-webflux-fn`, para convertirlo en un OAuth Resource Server y realizar tareas de autorización
- Using OAuth 2.0 with Spring WebClient
  - spring-6-webclient-oauth2
    - Vamos a añadir OAuth2 al proyecto `spring-6-webclient` para convertirlo en un cliente con OAuth2
    - Básicamente, con este cliente se hace lo mismo que con Postman, es decir, llamar a los endpoints, en este caso, del proyecto `spring-6-webflux-fn-resource-server`
- Spring Cloud Gateway
  - spring-6-gateway
    - Nuevo proyecto para crear un Gateway que, usando `spring-6-auth-server` va a llamar (hacer los requests) a los distintos proyectos `spring-6-resource-server`, que es el MVC, `spring-6-webflux-resource-server` y `spring-6-webflux-fn-resource-server`
- OpenAPI with Spring Boot
  - spring-6-openapi
    - Vamos a añadir al proyecto `spring-6-resource-server` la generación de OpenApi
    - Como ya tenemos el código, usaremos Code First (Specificacion First es preferible)
    - Vamos a obtener una página Swagger del tipo: `http://server:port/context-path/swagger-ui/index.html`
    - Vamos a obtener la descripción OpenAPI en una ruta tipo: `http://server:port/context-path/v3/api-docs`
    - Ver documentación (incluida en la siguiente sección)
- OpenAPI Validation with RestAssured
  - spring-6-openapi-restassured
    - Continuamos desde el proyecto `spring-6-openapi`
    - RestAssured es una biblioteca de testing muy popular para RESTful APIs. Vamos a hacer tests
    - Usando swagger-request-validator (ver documentación), creado por Atlassian (los de Jira), vamos a validar que nuestras peticiones casan con nuestra especificación OpenAPI
- Introduction to Spring AI
  - spring-6-ai
    - Nuevo proyecto con un ejemplo para usar la API Key de OpenAI
- Spring RestClient
  - spring-6-restclient
    - Cogemos toda la estructura del proyecto `spring-6-resttemplate-oauth2` para realizar este proyecto
    - RestClient sigue el estilo de programación de WebClient, pero es de naturaleza bloqueante, y realmente está construido sobre RestTemplate, a tal nivel que se puede instanciar un RestClient usando una instancia de RestTemplate

## URLs con documentación

- https://bootcamptoprod.com/spring-bean-life-cycle-explained/
- https://sfg-beer-works.github.io/brewery-api/
  - Para jugar con los endpoints
- https://github.com/json-path/JsonPath
  - DSL para leer documentos JSON. Se incluye en las dependencias de Spring Boot test
  - Lo vamos a usar para realizar aserciones contra un objeto JSON devuelto
- https://stackoverflow.com/questions/35722586/http-headers-accept-and-content-type-in-a-rest-context
- https://mapstruct.org/documentation/reference-guide/
- https://docs.spring.io/spring-boot/docs/2.1.x/reference/html/howto-database-initialization.html
- https://www.baeldung.com/database-migrations-with-flyway
- https://docs.spring.io/spring-boot/docs/2.0.0.M5/reference/html/howto-database-initialization.html#howto-execute-flyway-database-migrations-on-startup
- https://github.com/plotly/datasets
  - Distintos sets de data en formato .CSV para trabajar con ellos
- https://opencsv.sourceforge.net/
- https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
- https://github.com/spring-projects/spring-data-rest
  - Repositorio del proyecto Spring Data Rest. Se puede ir a la documentación oficial, hay ejemplo, se indica como configurar Maven...
- https://www.arquitecturajava.com/que-es-el-principio-de-hateoas/
- https://docs.spring.io/spring-data/rest/reference/etags-and-other-conditionals.html
- https://docs.spring.io/spring-authorization-server/reference/index.html
- https://www.jstoolset.com/jwt
  - Para decodificar JWT
- https://docs.spring.io/spring-data/relational/reference/r2dbc.html
- Validaciones muy personalizadas
  - https://blog.tericcabrel.com/write-custom-validator-for-body-request-in-spring-boot/
- Spring WebFlux fn
  - https://docs.spring.io/spring-framework/reference/web/webflux-functional.html
- Spring Boot Maven Plugin
  - https://docs.spring.io/spring-boot/docs/3.1.3/maven-plugin/reference/htmlsingle/
  - https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html
- Spring Boot Gradle Plugin
  - https://docs.spring.io/spring-boot/docs/3.0.1/gradle-plugin/reference/htmlsingle/
  - https://docs.gradle.org/current/userguide/build_lifecycle.html
- OpenAPI with Spring Boot
  - https://springdoc.org/#getting-started
- Swagger Request Validator
  - https://bitbucket.org/atlassian/swagger-request-validator/src/master/
