# spring-6-security-basic

Es el mismo proyecto `spring-6-db-relationships` (Spring MVC), donde vamos a securizar nuestras APIs.

HTTP Basic Authentication es una forma muy fácil y rápida de securizar y vamos a utilizar `Spring Security` para habilitarlo.

El problema es que las credenciales de seguridad, usuario y password, se pasan sin encriptar por Internet. La codificación en Base64 que puede usarse es muy fácil de decodificar.

Así que solo recomiendo usar este tipo de autenticación si tenemos habilitado HTTPS y no lo haría en un proyecto empresarial.

Lo que vamos a hacer en esta y las siguientes secciones, es configurar Spring Authentication Server y OAuth2, que utilizarán JWT.

## Notas

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql`
  - Importante si queremos usar MySql en vez de H2
- Ejecutar todos los tests