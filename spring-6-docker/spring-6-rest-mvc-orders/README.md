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

4. Ejecutar MySql en Docker

Ejecutar el terminal el mandato:

`docker run --name mysql -d -e MYSQL_USER=restadmin -e MYSQL_PASSWORD=password -e MYSQL_DATABASE=restdb -e MYSQL_ROOT_PASSWORD=password mysql:8`

Yo esto no lo hago ya que ya tengo en una Raspberry Pi MySQL corriendo en Docker, y este proyecto usa ese MySql.

5. Ejecutar el contenedor RestMVC con MySql

Tenemos que mirar/configurar el archivo `application-localmysql.properties` y ejecutar lo siguiente:

```shell
docker stop rest-mvc
docker rm rest-mvc
docker run --name rest-mvc -d -p 8081:8080 --platform linux/amd64 -e SPRING_PROFILES_ACTIVE=localmysql \
 -e SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://auth-server:9000 -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/restdb  \
 -e SERVER_PORT=8080 --link auth-server:auth-server --link mysql:mysql restmvc:0.0.1-SNAPSHOT
```

Este mandato lo cambio para que tire del MySql que tengo en mi Raspberry Pi, es decir, esta es fake (ver archivo `docker/RunDockerContainerRestMVCMySql.md)