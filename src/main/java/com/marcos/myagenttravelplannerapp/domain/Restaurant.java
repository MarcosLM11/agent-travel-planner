package com.marcos.myagenttravelplannerapp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record Restaurant(
        @JsonPropertyDescription("Name of the restaurant")
        String name,

        @JsonPropertyDescription("Type of cuisine served")
        String cuisine,

        @JsonPropertyDescription("Price range, e.g. '$', '$$', '$$$'")
        String priceRange,

        @JsonPropertyDescription("Neighborhood or area where the restaurant is located")
        String neighborhood,

        @JsonPropertyDescription("Special notes such as reservations required, dress code, best dishes")
        String specialNotes
) {
}
