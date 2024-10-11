package com.jmunoz.springaiintro.services;

import com.jmunoz.springaiintro.model.*;

public interface OpenAIService {

    String getAnswer(String question);

    Answer getAnswer(Question question);

    Answer getCapital(GetCapitalRequest getCapitalRequest);

    GetCapitalResponse getCapital2(GetCapitalRequest getCapitalRequest);

    Answer getCapitalWithInfo(GetCapitalRequest getCapitalRequest);

    GetCapitalWithInfoResponse getCapitalWithInfo2(GetCapitalRequest getCapitalRequest);
}
