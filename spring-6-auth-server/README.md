# spring-6-auth-server

Es un nuevo proyecto en el que vamos a conseguir autorización usando el framework OAuth 2 y JWT, en vez de usar la seguridad básica que ya hemos visto.

Los pasos para lograr esto son:

- Configurar un server de autenticación usando el proyecto Spring Authentication Server
- Configurar un resource server
- Como puede un RestTemplate obtener el token y usarlo para autenticarse

Vamos a ver en concreto el primer paso.

Además, para no crear otro proyecto, porque es muy sencillo, añadimos Spring Boot Actuator aquí mismo.

## Notas

1. Para trabajar con el proyecto Spring Authentication Server hay que añadir la siguiente dependencia al POM y se puede seguir la documentación

https://docs.spring.io/spring-authorization-server/reference/getting-started.html

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-authorization-server</artifactId>
</dependency>
```

2. En este proyecto se ha tenido que importar muchos paquetes.

IntelliJ lo puede hacer automáticamente con esta configuración.

![alt AutoImport](../images/15-IntelliJ-AutoImport.png)

3. En la ejecución puede verse claramente. Este proyecto se hace por motivos didácticos y nunca para usar en producción. 

![alt AutoImport](../images/16-Authorization-Server-Execution.png)

4. Configuración de Postman para generar un token usando Client Credentials.

En el endpoint GET se ha indicado un endpoint cualquiera.

En Auth Type se ha seleccionado OAuth 2.0 y se han informado los valores.

![alt AutoImport](../images/17-Postman-GetToken-Oauth2.png)

Para obtener el token, pulsar `Get new access token` y copiarlo.

Ir a `https://www.jstoolset.com/jwt` y pegarlo. Se debe ver algo tipo así.

![alt AutoImport](../images/18-JWT-Decoder.png)

NOTA: No usar esta página para JWT de la empresa, puesto que no sabemos que hace con él

5. Añadir la dependencia para Spring Boot Actuator

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Se ha añadido la configuración de seguridad en `SecurityConfig.java`.

6. Añadir Integración de Actuator para las sondas en Kubernates

Se hace vía properties.

```
management.endpoint.health.probes.enabled=true
management.health.readinessstate.enabled=true
management.health.livenessstate.enabled=true
```

## Testing

- Clonar el repositorio
- Ejecutar esta aplicación
- Uso de Postman para obtener un token de nuestro authorization server
- Para probar `Spring Boot Actuator`, ejecutar este proyecto e ir a Postman
    - En la carpeta `/postman/actuator` tenemos los distintos endpoints