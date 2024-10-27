# spring-6-auth-server

Dockerización.

## Notas

1. Dockerizar este proyecto

Para construir una imagen, ir a la terminal, a la carpeta donde está este proyecto de auth-server y ejecutar:

`./mvnw clean package spring-boot:build-image`

2. Ejecutar la imagen

Ejecutar en la terminal el comando:

`docker run --name auth-server --platform linux/amd64 -d -p 9000:9000 spring-6-auth-server:0.0.1-SNAPSHOT`

3. Pruebas en Postman

Ahora podemos ir a `Postman` para confirmar que accedemos a esa imagen:

- Importar en Postman el fichero de la carpeta `/postman` y ejecutar `Generate Token`
