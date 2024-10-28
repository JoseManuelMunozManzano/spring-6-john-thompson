# spring-6-docker-compose

Esta es la carpeta contenedora de los proyectos que se van a dockerizar usando `docker compose`, que son:

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

La imagen a tener en la cabeza es esta:

![alt Docker](./images/SpringGateway.jpg)

Todos estos proyectos pueden encontrarse en el directorio `spring-6-docker` y no se vuelven a copiar aquí porque no son necesarios.

Solo es necesario crear las imágenes Docker de estos proyectos.

## Testing

- Clonar este repositorio
- Llevar la carpeta spring-6-docker-compose a IntelliJ para que tenga todos los proyectos
- Crear las imágenes de cada proyecto (los 5 de arriba), usando el comando `./mvnw clean package spring-boot:build-image` sobre cada uno de los proyectos de la carpeta `spring-6-docker`
- En el directorio `docker-compose` se ha creado el archivo `compose.yaml`, que es el que contendrá toda la info para dockerizar usando docker compose
  - Yo ya tengo MySql y Mongo dockerizados en una Raspberry Pi, por lo que no tengo que volver a crearlos aquí
  - Acceder a ese directorio y renombrar `template_compose.yaml` a `compose.yaml` sustituyendo `mysql` y `mymongo` por la configuración correcta
- Ejecutar con el mandato `docker compose up -d`
- Parar los contenedores con el mandato `docker compose stop`
- Parar, eliminar los contenedores y la network usando el mandato `docker compose down`
- Para probar en Postman, tenemos que pedir un token (spring-6-auth-sever, importar carpeta postman)
- Luego probar para cada versión
  - GET: http://localhost:8080/api/v1/beer
  - GET: http://localhost:8080/api/v2/beer
  - GET: http://localhost:8080/api/v3/beer
