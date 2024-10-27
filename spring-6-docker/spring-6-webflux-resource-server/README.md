# spring-6-webflux-resource-server

Dockerización.

## Notas

1. Dockerizar este proyecto

Para construir una imagen, ir a la terminal, a la carpeta donde está este proyecto de webflux-resource-server y ejecutar:

`./mvnw clean package spring-boot:build-image`

2. Ejecutar el contenedor

Ejecutar en la terminal el comando:

`docker run --name reactive -d -p 8082:8080 --platform linux/amd64 -e SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://auth-server:9000 -e SERVER_PORT=8080 --link auth-server:auth-server spring6datar2dbc:0.0.1-SNAPSHOT`

Ahora tenemos que reconstruir el gateway para linkar auth-server, rest-mvc y reactive

```shell
docker stop gateway 
docker rm gateway
docker run --name gateway -d -p 8080:8080 --platform linux/amd64 -e SPRING_PROFILES_ACTIVE=docker --link auth-server:auth-server --link rest-mvc:rest-mvc --link reactive:reactive spring6gateway:0.0.1-SNAPSHOT
```

Con esto ya podemos probar en Postman.