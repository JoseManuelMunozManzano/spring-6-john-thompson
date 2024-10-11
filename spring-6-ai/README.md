# spring-6-ai

Nuevo proyecto con un ejemplo para usar la API Key de OpenAI.

## Notas

1. Es necesario tener una API Key de OpenAI.

Esta API Key la vamos a pasar al programa usando variables de entorno usando IntelliJ.

Esa variable de entorno alimenta una propiedad de `application.properties`. En concreto hay que crear la variable de entorno `OPENAI_API_KEY`, cuyo valor pasa a la propiedad `spring.ai.openapi.api-key`: 

```
spring.ai.openai.api-key=${OPENAI_API_KEY}
```

En IntelliJ, las variables de entorno se indican en la configuración de la ejecución de la aplicación:

![alt Environment Variables](../images/23-IntelliJ-EnvironmentVariables.png)

2. Configurando un template (JUnit) para usar también la API Key de OpenAI.

Tenemos que configurar también la variable de entorno para ejecutar los tests. Vamos a usar IntelliJ.

Para ello, vamos a la configuración de la ejecución de la aplicación y, abajo a la izquierda veremos `Edit configuration templates...`

Lo seleccionamos y en la ventana que nos aparece, seleccionamos `JUnit` e indicamos la variable de entorno

![alt Configuration Templates and Environment Variables](../images/24-IntelliJ-EditConfigurationTemplates-EnvironmentVariables.png)

3. Todas estas configuraciones se pueden hacer también usando configuración de Maven.

4. Vemos en la carpeta `resources`, que existe la carpeta `templates` donde creamos plantillas de texto.

Se ha creado la plantilla `get-capital-prompt.st` y se sustituye el texto de promptTemplate por el texto indicado en esta plantilla.

```
    @Value("classpath:templates/get-capital-prompt.st")
    private Resource getCapitalPrompt;
    
    @Override
    public Answer getCapital(GetCapitalRequest getCapitalRequest) {
        PromptTemplate promptTemplate = new PromptTemplate(getCapitalPrompt);
        
        // Este valor de key "stateOrCountry" debe ser el mismo que el indicado entre llaves en get-capital-prompt
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", getCapitalRequest.stateOrCountry()));        
        ...
    }
```

Vemos en la plantilla `get-capital-with-info.st` que se permiten las marcas de triple tilde ```

## Testing

- Clonar el repositorio
- Renombrar `application.template.properties` a `application.properties`
- Indicar en IntelliJ el valor de la variable de entorno `OPENAI_API_KEY`
  - Ver las notas 1 y 2 un poco mås arriba en esta documentación
- Ejecutar el test `OpenAIServiceImplTest.java`
- Por otra parte, se puede ejecutar el programa, y probar usando Postman (ver la carpeta postman)