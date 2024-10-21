# spring-6-rest-mvc-events

Dado nuestro proyecto `spring-6-resource-server`, vamos a crear un nuevo proyecto y trabajar con eventos

Se basa en el patrón Observer.

Vamos a trabajar en un caso de uso para usar application events para crear un log de auditoría.

Cada vez que creamos, actualizamos o borramos un Objecto Beer, vamos a lanzar un evento.

## Notas

1. Los Pojos para los events se crean en el package `events`.

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql`
  - Importante si queremos usar MySql en vez de H2
- Ejecutar también el proyecto `spring-6-auth-server`
  - Obtener un token usando el endpoint que está en su carpeta `postman`, y, una vez obtenido el token, pulsar `Use Token`