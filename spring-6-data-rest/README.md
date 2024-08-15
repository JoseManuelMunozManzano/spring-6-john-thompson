# spring-6-data-rest

Es un nuevo ejemplo (no viene de ejemplos anteriores) de, solo definiendo nuestras entidades y los repositorios, se genera una creación automática de controladores.

Spring Data REST es una herramienta que podemos usar para exponer la BD o entidades de la BD solo usando repositorios.

Es una espada de doble filo, porque vamos a tener que exponer las entidades de BD directamente al consumidor de la API.

Esto no vamos a querer hacerlo siempre, pero es muy útil para proveer una forma rápida de desarrollo de API RESTFul.

## Notas

1. Vamos a construir un ejemplo muy parecido a este: `https://sfg-beer-works.github.io/brewery-api/#tag/Beer-Service/operation/listBeers`

2. Para el ejemplo se usa la BD en memoria H2

3. Aunque en el Github del proyecto se indica otra cosa (spring-data-rest), en el POM se añade la dependencia siguiente, ya que viene curada para Spring Boot

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-rest</artifactId>
</dependency>
```

## Testing

- Clonar el repositorio