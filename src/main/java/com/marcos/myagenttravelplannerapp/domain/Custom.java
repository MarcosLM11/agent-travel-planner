package com.marcos.myagenttravelplannerapp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record Custom(
        @JsonPropertyDescription("Name of the local custom")
        String name,

        @JsonPropertyDescription("Description of the custom and its cultural significance")
        String description,

        @JsonPropertyDescription("What to do and what NOT to do regarding this custom")
        String doAndDont
) {
}
