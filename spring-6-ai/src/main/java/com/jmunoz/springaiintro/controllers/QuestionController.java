package com.jmunoz.springaiintro.controllers;

import com.jmunoz.springaiintro.model.Answer;
import com.jmunoz.springaiintro.model.GetCapitalRequest;
import com.jmunoz.springaiintro.model.GetCapitalResponse;
import com.jmunoz.springaiintro.model.Question;
import com.jmunoz.springaiintro.services.OpenAIService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {

    private final OpenAIService openAIService;

    public QuestionController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question) {
        return openAIService.getAnswer(question);
    }

    @PostMapping("/capital")
    public Answer getCapital(@RequestBody GetCapitalRequest getCapitalRequest) {
        return this.openAIService.getCapital(getCapitalRequest);
    }

    @PostMapping("/capital2")
    public GetCapitalResponse getCapital2(@RequestBody GetCapitalRequest getCapitalRequest) {
        return this.openAIService.getCapital2(getCapitalRequest);
    }

    @PostMapping("/capitalWithInfo")
    public Answer getCapitalWithInfo(@RequestBody GetCapitalRequest getCapitalRequest) {
        return this.openAIService.getCapitalWithInfo(getCapitalRequest);
    }
}
