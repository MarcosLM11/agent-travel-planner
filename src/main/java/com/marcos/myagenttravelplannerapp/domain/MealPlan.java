package com.marcos.myagenttravelplannerapp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record MealPlan(
        @JsonPropertyDescription("Breakfast suggestion")
        MealSuggestion breakfast,

        @JsonPropertyDescription("Lunch suggestion")
        MealSuggestion lunch,

        @JsonPropertyDescription("Dinner suggestion")
        MealSuggestion dinner
) {
}
