# spring-6-docker

Esta es la carpeta contenedora de los proyectos que se van a dockerizar, que son:

- spring-6-auth-server
  - Puerto 9000
- spring-6-gateway
  - Puerto 8080
- spring-6-rest-mvc-orders
  - Puerto 8081 y usando MySql
- spring-6-webflux-resource-server
  - Puerto 8082 y usando H2
- spring-6-webflux-fn-resource-server
  - Puerto 8083 y usando MongoDB

La imagen a tener en la cabeza es esta>

![alt Docker](./images/SpringGateway.jpg)

## Notas

1. En la carpeta `docker` pueden encontrarse distintas ayudas de comandos

## Testing

- Clonar este repositorio
- Llevar la carpeta spring-6-docker a IntelliJ para que tenga todos los proyectos