# spring-6-data-r2dbc

Vamos a ver un proyecto de ejemplo de operaciones de persistencia de datos contra una BD usando el framework Spring Data R2DBC.
    
Usa drivers reactivos para BD relacionales.

Esta tecnología reactiva contra la BD es todavía muy reciente.

En este proyecto reactivo creamos las mismas tablas que creamos en el proyecto MVC.

## Notas

1. Documentación

https://docs.spring.io/spring-data/relational/reference/r2dbc.html

## Testing

- Clonar el repositorio
- En la carpeta `resources` existe el archivo `schema.sql` que se ejecuta automáticamente al arrancar el proyecto, gracias a la clase de configuración `DatabaseConfig.java`
- Ejecutar el proyecto