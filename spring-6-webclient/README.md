# spring-6-webclient

WebClient es la tecnología que quiere sustituir a RestTemplate. Usa tecnología reactiva.

Al igual que RestTemplate, con WebClient podemos hacer llamadas a un back, es decir, WebClient nos sirve de cliente.

## Notas

1. Para usar `awaitility` hay que añadir al POM la siguiente dependencia

```
<dependency>
    <groupId>org.awaitility</groupId>
    <artifactId>awaitility</artifactId>
    <version>4.2.2</version>
</dependency>
```

## Testing

- Clonar el repositorio
- Ejecutar el proyecto `spring-6-webflux-fn` que será nuestro back
- Ejecutar este proyecto `spring-6-webclient` que será nuestro client
- Ejecutar el test `BeerClientImplTest`
  - De nuevo, no olvidar ejecutar el proyecto `spring-6-webflux-fn` que será nuestro back