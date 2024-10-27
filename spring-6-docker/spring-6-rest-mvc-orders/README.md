# spring-6-rest-mvc-orders

Dockerización.

## Notas

1. Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores

2. Dockerizar este proyecto

Para construir una imagen, ir a la terminal, a la carpeta donde está este proyecto de rest-mvc-orders y ejecutar:

`./mvnw clean package spring-boot:build-image`