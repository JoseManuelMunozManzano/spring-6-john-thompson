package com.jmunoz.springaiintro.services;

import com.jmunoz.springaiintro.model.Answer;
import com.jmunoz.springaiintro.model.Question;

public interface OpenAIService {

    String getAnswer(String question);

    Answer getAnswer(Question question);
}
