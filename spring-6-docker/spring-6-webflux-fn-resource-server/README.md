# spring-6-webflux-fn-resource-server

Dockerización.

## Notas

1. - Renombrar `application.properties_template` a `application.properties` e indicar sus valores

2. Dockerizar este proyecto

Para construir una imagen, ir a la terminal, a la carpeta donde está este proyecto de webflux-fn-resource-server y ejecutar:

`./mvnw clean package spring-boot:build-image`