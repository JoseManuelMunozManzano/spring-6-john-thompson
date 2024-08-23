# spring-6-auth-server

Es un nuevo proyecto en el que vamos a conseguir autorización usando el framework OAuth 2 y JWT, en vez de usar la seguridad básica que ya hemos visto.

Los pasos para lograr esto son:

- Configurar un server de autenticación usando el proyecto Spring Authentication Server
- Configurar un resource server
- Como puede un RestTemplate obtener el token y usarlo para autenticarse

Vamos a ver en concreto el primer paso

## Notas

1. Para trabajar con el proyecto Spring Authentication Server hay que añadir la siguiente dependencia al POM 

```
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-oauth2-authorization-server</artifactId>
</dependency>
```

## Testing

- Clonar el repositorio