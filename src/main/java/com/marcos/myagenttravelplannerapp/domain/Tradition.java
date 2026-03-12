package com.marcos.myagenttravelplannerapp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record Tradition(
        @JsonPropertyDescription("Name of the tradition")
        String name,

        @JsonPropertyDescription("Description of the tradition and how it is celebrated")
        String description,

        @JsonPropertyDescription("Why this tradition is important for the traveler to know about")
        String relevance
) {
}
