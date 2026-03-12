package com.marcos.myagenttravelplannerapp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.util.List;

public record CulturalInsights(
        @JsonPropertyDescription("Local traditions the traveler should know about")
        List<Tradition> traditions,

        @JsonPropertyDescription("Local customs and social norms")
        List<Custom> customs,

        @JsonPropertyDescription("Etiquette notes and tips for respectful behavior")
        List<String> etiquetteNotes,

        @JsonPropertyDescription("Useful phrases in the local language with translations")
        List<LocalPhrase> localPhrases,

        @JsonPropertyDescription("Festivals and events happening during the travel dates")
        List<Event> festivalsAndEvents
) {
}
