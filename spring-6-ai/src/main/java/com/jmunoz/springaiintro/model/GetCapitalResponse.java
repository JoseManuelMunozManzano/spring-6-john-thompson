package com.jmunoz.springaiintro.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

// Para generar un esquema JSON de este record, se añade @JsonPropertyDescription
public record GetCapitalResponse(@JsonPropertyDescription("This is the city name") String answer) {
}
