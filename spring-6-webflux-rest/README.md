# spring-6-webflux-rest

Continuamos el ejemplo `spring-6-data-r2dbc`. Una vez tenemos persistencia hecha de manera reactiva, vamos a hacer la funcionalidad web.
    
Spring WebFlux MVC tiene casi las mismas anotaciones que encontramos en Spring MVC. Esto se ha hecho intencionalmente para que los desarrolladores tengan una transición más suave a la pila reactiva.

Vamos a crear controladores reactivos y vamos a hacer tests a esos controladores.

También vamos a crear mapeos con MapStruct, Services, usar Path Variables...

El truco es: excepciones y programación reactiva son eventos que necesitamos escuchar y gestionar.

## Notas

1. Netty

Con Spring WebFlux, se ejecuta el web server Netty. Recordar que con Spring MVC se ejecutaba el web server Tomcat.

2. MapStruct

Se ve como se configura MapStruct en el fichero `pom.xml`. No olvidar refrescar Maven.

Una vez creados los mappers, se puede ir al ciclo de vida de `Maven` y ejecutar los pasos `clean` y `compile`. En la carpeta `target` se pueden ver las implementaciones de los mappers.

## Testing

- Clonar el repositorio
- En la carpeta `resources` existe el archivo `schema.sql` que se ejecuta automáticamente al arrancar el proyecto, gracias a la clase de configuración `DatabaseConfig.java`
- Ejecutar el proyecto
- En la carpeta `postman` se encuentra un archivo que hay que importar a Postman e ir ejecutando