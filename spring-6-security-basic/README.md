# spring-6-security-basic

Es el mismo proyecto `spring-6-db-relationships` (Spring MVC), donde vamos a securizar nuestras APIs.

HTTP Basic Authentication es una forma muy fácil y rápida de securizar y vamos a utilizar `Spring Security` para habilitarlo.

El problema es que las credenciales de seguridad, usuario y password, se pasan sin encriptar por Internet. La codificación en Base64 que puede usarse es muy fácil de decodificar.

Así que solo recomiendo usar este tipo de autenticación si tenemos habilitado HTTPS (encriptación por protocolo), aunque jamás lo utilizaría en un proyecto empresarial.

Lo que vamos a hacer en esta y las siguientes secciones, es configurar Spring Authentication Server y OAuth2, que utilizarán JWT.

## Notas

1. Una vez añadida la dependencia en el POM, Spring Boot autoconfigura HTTP Basic Security y securiza todos los endpoints.

Por defecto, el username es `user`.

Y, en esta imagen de la ejecución del proyecto, puede verse que se ha autogenerado un password (cambia con cada ejecución) para desarrollo usando Base64.

![alt Basic Authentication](../images/13-SpringSecurity-Basic-Authentication.png)

2. Configurar Postman para que tenga en cuenta HTTP Basic Security

![alt Basic Authentication](../images/14-Postman-Basic-Auth.png)

3. Cómo configurar el usuario y el password para evitar que sean las que da Spring Security por defecto.

Se configuran en `application.properties`.

Ahora deberemos cambiar el Username y el Password de Postman a estos valores seleccionados por nosotros.

4. Testing Spring Security con JUnit5

Obviamente, el hecho de haber añadido seguridad, cambia la naturaleza de nuestros tests.

Cuando en nuestros tests no traemos el contexto de aplicación web, usando `@WebMvcTest`, los tests fallan.

Es decir, un contexto donde tenemos mock falla porque obtiene por defecto el filtro de Spring Security, mientras que un contexto con `@SpringBootTest` no obtiene el filtro de Spring Security y por eso el test sigue funcionando, aunque lo correcto es que ¡deberían fallar!

Por tanto, tenemos dos problemas:

- Se añade el filtro
- Nuestros tests no tienen el user

Para testing se añade una nueva dependencia:

```
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
</dependency>
```

5. Testing. Clase de configuración para deshabilitar CSRF

Para poder testear endpoints distintos a GET, se debe deshabilitar CSRF. Si no, el test arroja un error 403.

Esto se hace con una clase de configuración (ver `config/SpringSecConfig.java`) y se añade un Spring Security Filter Chain para deshabilitar CSRF.

Luego se importa esta clase de configuración al test (ver `BeerControllerTest.java`)

6. Testing. Clases de test IT

Para que se aplique la seguridad, al construir mockMvc, hay que aplicar `springSecurity()`

```
@BeforeEach
void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .apply(springSecurity())
            .build();
}
```

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql`
  - Importante si queremos usar MySql en vez de H2
- Ejecutar los tests