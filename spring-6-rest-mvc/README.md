# spring-6-rest-mvc

Vamos a hacer un ejemplo de app REST MVC.

## Notas

Para añadir Live Reload al proyecto hay que añadir al POM la dependencia `DevTools`:

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
</dependency>
```

Y para que funcione en IntelliJ hay que habilitar lo siguiente en Settings:

![alt DevTools](../images/03-DevTools.png)

## Testing

- Clonar el repositorio
- Ejecutar el proyecto
- Importar la carpeta postman a Postman y probar los endpoints
  - Get All Bears
  - Coger uno de los Id y probar Get Beer By Id
  - Get All Customers
  - Coger uno de los Id y probar Get Customer By Id
  - Add New Beer