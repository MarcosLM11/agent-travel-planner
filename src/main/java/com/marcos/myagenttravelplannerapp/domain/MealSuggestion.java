package com.marcos.myagenttravelplannerapp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record MealSuggestion(
        @JsonPropertyDescription("Recommended restaurant or food spot")
        String restaurant,

        @JsonPropertyDescription("Recommended dish to try")
        String dish,

        @JsonPropertyDescription("Alternative option for dietary restrictions or preference")
        String alternativeOption,

        @JsonPropertyDescription("Expected price range for this meal")
        String priceRange
) {
}
