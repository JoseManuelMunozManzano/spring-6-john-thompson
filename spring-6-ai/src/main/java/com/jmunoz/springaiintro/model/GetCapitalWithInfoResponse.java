package com.jmunoz.springaiintro.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record GetCapitalWithInfoResponse(@JsonPropertyDescription("The name of the city") String city,
                                         @JsonPropertyDescription("The population of the city") Integer population,
                                         @JsonPropertyDescription("The region the city is located in") String region,
                                         @JsonPropertyDescription("The primary language spoken") String languaje,
                                         @JsonPropertyDescription("The currency used") String currency) {
}
