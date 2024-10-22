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

4. Otras cosas que hacer

Hasta el punto 3 sería todo lo necesario para crear un evento, pero Spring también tiene soporte para testear eventos.

En nuestro test del punto 3, hicimos una acción que dispara el evento para ser publicado, pero en dicho test no está claro que el evento realmente se ha publicado.

Para habilitar ese soporte, añadimos a la clase `BeerControllerIT` la anotación `@RecordApplicationEvents`.

5. Procesamiento de Eventos Asíncrono

En todo lo visto en los puntos anteriores, los eventos se ejecutaban en el mismo hilo, de forma síncrona.

Este es el funcionamiento por defecto.

Pero queremos escribir un registro de auditoría, y este proceso lo queremos independiente de la transacción, desacoplado y por tanto asíncrono.

Para hacer un evento asíncrono, lo primero es añadir un TaskExecutor. Lo hacemos en el package `config`, con nombre `TaskConfig.java`.

Luego, en `BeerCreatedListener.java` tenemos que añadir la anotación `@Async`.

## Testing

- Clonar el repositorio
- Renombrar `application-localmysql.template.properties` a `application-localmysql.properties` e indicar sus valores
- Ejecutar el test `BeerControllerIT`, en concreto el método `testCreateBeerMVC()`
- Para probar la parte de los eventos asíncronos, ejecutar de nuevo el mismo test (punto anterior) y comprobar que se escribe en consola el thread y su id
  - El nombre/id del Thread de `BeerServiceJPA` debe ser distinto al de `BeerCreatedListener`
- Para probar la creación de la tabla `beerAuditEntity`, por ahora se hace con la BBDD H2 en memoria
  - Por tanto, ejecutar el proyecto sin activar el profile de mysql, es decir, quitar `-Dspring.profiles.active=localmysql`