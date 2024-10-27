# spring-6-webflux-fn-resource-server

Dockerización.

## Notas

1. - Renombrar `application.properties_template` a `application.properties` e indicar sus valores

2. Dockerizar este proyecto

Para construir una imagen, ir a la terminal, a la carpeta donde está este proyecto de webflux-fn-resource-server y ejecutar:

`./mvnw clean package spring-boot:build-image`

3. Ejecutar Reactive Mongo

Yo ya estoy ejecutando Mongo usando mi Raspberry Pi, estando Mongo ejecutándose en un contenedor Docker.

Este comando no tengo que ejecutarlo por lo que digo en la línea anterior.

```shell
docker run --name reactive-mongo -d -p 8083:8080 --platform linux/amd64 -e SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://auth-server:9000 \
 -e MONGO_ADDRESS=mongo -e SERVER_PORT=8080 --link auth-server:auth-server --link mongo:mongo reactivemongo:0.0.1-SNAPSHOT
```

Este comando lo cambio para que tire del Mongo que tengo en mi Raspberry Pi, es decir, este es fake (ver archivo `docker/RunDockerContainerReactiveMongo.md)