# spring-6-rest-mvc-events

Dado nuestro proyecto `spring-6-resource-server`, vamos a crear un nuevo proyecto y trabajar con eventos

Se basa en el patrón Observer.

Vamos a trabajar en un caso de uso para usar application events para crear un log de auditoría.

Cada vez que creamos, actualizamos o borramos un Objecto Beer, vamos a lanzar un evento.

## Notas

1. Los Pojos para los events se crean en el package `events`.

2. Luego creamos los event listeners, que van a escuchar cuando el POJO se lanza al application event context. Se crean en el package `listeners`.

3. Luego publicamos el application event.

Ver el test `BeerControllerIT`, nuevo método `testCreateBeerMVC()`, que implementamos en el package `services`, clase `BeerServiceJPA`, método `saveNewBeer()`.

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el proyecto con el siguiente profile activo `-Dspring.profiles.active=localmysql`
  - Importante si queremos usar MySql en vez de H2
- Ejecutar el test `BeerControllerIT`, en concreto el método `testCreateBeerMVC()`