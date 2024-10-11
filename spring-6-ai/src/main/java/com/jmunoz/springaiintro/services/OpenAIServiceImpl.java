package com.jmunoz.springaiintro.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmunoz.springaiintro.model.Answer;
import com.jmunoz.springaiintro.model.GetCapitalRequest;
import com.jmunoz.springaiintro.model.GetCapitalResponse;
import com.jmunoz.springaiintro.model.Question;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OpenAIServiceImpl implements OpenAIService {

    private final ChatModel chatModel;

    @Value("classpath:templates/get-capital-prompt.st")
    private Resource getCapitalPrompt;

    @Value("classpath:templates/get-capital2-prompt.st")
    private Resource getCapital2Prompt;

    @Value("classpath:templates/get-capital-with-info.st")
    private Resource getCapitalPromptWithInfo;

    // En el template get-capital-with-prompt.st indicamos que la respuesta la de como JSON
    // Necesitamos este ObjectMapper para el JSON
    @Autowired
    ObjectMapper objectMapper;

    public OpenAIServiceImpl(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public String getAnswer(String question) {
        PromptTemplate promptTemplate = new PromptTemplate(question);
        Prompt prompt = promptTemplate.create();

        ChatResponse response = chatModel.call(prompt);

        return response.getResult().getOutput().getContent();
    }

    @Override
    public Answer getAnswer(Question question) {
        PromptTemplate promptTemplate = new PromptTemplate(question.question());
        Prompt prompt = promptTemplate.create();

        ChatResponse response = chatModel.call(prompt);

        return new Answer(response.getResult().getOutput().getContent());
    }

    @Override
    public Answer getCapital(GetCapitalRequest getCapitalRequest) {
        // Sustituimos este PrompTemplate por el archivo `get-capital-prompt.st` situado en la carpeta
        // `resources/templates`.
        // PromptTemplate promptTemplate = new PromptTemplate("What is the capital of " + getCapitalRequest.stateOrCountry() + "?");
        PromptTemplate promptTemplate = new PromptTemplate(getCapitalPrompt);
        // Este valor de key "stateOrCountry" debe ser el mismo que el indicado entre llaves en get-capital-prompt.
        // Así se enlaza el valor del record a la key
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", getCapitalRequest.stateOrCountry()));

        ChatResponse response = chatModel.call(prompt);

        // Parte de gestión de devolución de la respuesta. Dado el JSON, se pasa a responseString
        System.out.println(response.getResult().getOutput().getContent());
        String responseString;
        try {
            JsonNode jsonNode = objectMapper.readTree(response.getResult().getOutput().getContent());
            responseString = jsonNode.get("answer").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }

        return new Answer(responseString);
    }

    // Utilizamos un schema JSON
    @Override
    public GetCapitalResponse getCapital2(GetCapitalRequest getCapitalRequest) {
        // En vez de utilizar JSON, utilizamos un BeanOutputConverter, que lo que hace
        // es crear un JSON schema, así que es mucho más detallado que lo que hicimos en getCapital()
        BeanOutputConverter<GetCapitalResponse> converter = new BeanOutputConverter<>(GetCapitalResponse.class);
        String format = converter.getFormat();
        System.out.println("Format: \n" + format);

        PromptTemplate promptTemplate = new PromptTemplate(getCapital2Prompt);
        // Indicamos "format", que aparece en la plantilla get-capital2-prompt.st
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", getCapitalRequest.stateOrCountry(), "format", format));

        ChatResponse response = chatModel.call(prompt);

        System.out.println(response.getResult().getOutput().getContent());

        return converter.convert(response.getResult().getOutput().getContent());
    }

    // Ver el template get-capital-with-info.st
    // Aquí no hay JSON ni schema, la salida es de tipo Answer, que tiene como atributo un string.
    @Override
    public Answer getCapitalWithInfo(GetCapitalRequest getCapitalRequest) {
        PromptTemplate promptTemplate = new PromptTemplate(getCapitalPromptWithInfo);
        Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", getCapitalRequest.stateOrCountry()));
        ChatResponse response = chatModel.call(prompt);

        return new Answer(response.getResult().getOutput().getContent());
    }
}
