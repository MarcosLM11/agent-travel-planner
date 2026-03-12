package com.marcos.myagenttravelplannerapp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record LocalPhrase(
        @JsonPropertyDescription("Phrase in the local language")
        String phrase,

        @JsonPropertyDescription("Translation to the traveler's preferred language")
        String translation,

        @JsonPropertyDescription("When and how to use this phrase")
        String context
) {
}
