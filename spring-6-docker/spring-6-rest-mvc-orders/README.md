# spring-6-rest-mvc-orders

Dockerización.

## Notas

1. Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores

2. Dockerizar este proyecto

Para construir una imagen, ir a la terminal, a la carpeta donde está este proyecto de rest-mvc-orders y ejecutar:

`./mvnw clean package spring-boot:build-image`

3. Ejecutar el contenedor

Ejecutar en la terminal el comando:

`docker run --name rest-mvc -d -p 8081:8080 --platform linux/amd64 -e SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://auth-server:9000 --link auth-server:auth-server restmvc:0.0.1-SNAPSHOT`

Ahora tenemos que reconstruir el gateway para linkar auth-server y rest-mvc

```shell
docker stop gateway 
docker rm gateway
docker run --name gateway -d -p 8080:8080 --platform linux/amd64 -e SPRING_PROFILES_ACTIVE=docker --link auth-server:auth-server --link rest-mvc:rest-mvc spring6gateway:0.0.1-SNAPSHOT
```