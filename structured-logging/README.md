# structured-logging

Nuevo proyecto para ver como funciona Structured Logging en la versión de Spring Boot 3.4.0.

## Notas

1. Vamos a configurar una clase Java que se ejecutará cuando iniciemos el proyecto.

Esto se hace implementando en la clase `CommandLineRunner`.

2. Cambiar la forma de salida de registros de Spring a JSON

Hay que tocar `application.properties` para añadir la siguiente property:

```
logging.structured.format.console=ecs
```

ecs significa `elastic common schema`.

Spring Boot soporta también los valores `logstash`, que también es JSON, pero muestra propiedades ligeramente diferentes, y `gelf` que significa `Greylog extended format`.

No hay que añadir ninguna dependencia para esto.

Con esta propiedad, en vez de en consola, podemos mandar estos logs a fichero:

```
logging.structured.format.file=gelf
```

Se ha dado el valor `gelf`, pero podría haber indicado cualquiera de los tres valores que hemos visto.

3. Podemos construir nuestro propio formato personalizado de log

Ver clase Java `KeyValueLogger`, que implementa `StructuredLogFormatter<ILoggingEvent>`.

Y en `application.properties` indicamos la siguiente propiedad:

```
logging.structured.format.console=com.jmm.structuredlogging.logstuff.KeyValueLogger
```

Siendo `com.jmm.structuredlogging.logstuff` el paquete donde está la clase `KeyValueLogger`.

3. Podemos construir nuestro propio formato personalizado de log en formato JSON

Ver la clase Java `JsonLogger` que, de nuevo, implementa `StructuredLogFormatter<ILoggingEvent>`.

Y en `application.properties` indicamos la siguiente propiedad con el package y el nombre de la clase:

```
logging.structured.format.console=com.jmm.structuredlogging.logstuff.JsonLogger
```

## Testing

- Clonar el repositorio
- Ejecutar el proyecto para ver los distintos tipos de logs