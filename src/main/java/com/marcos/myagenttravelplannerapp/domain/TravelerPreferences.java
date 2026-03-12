package com.marcos.myagenttravelplannerapp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.util.List;

public record TravelerPreferences(
        @JsonPropertyDescription("User interests such as history, gastronomy, nature, art, nightlife, sports, etc.")
        List<String> interests,

        @JsonPropertyDescription("Budget level for the trip")
        BudgetLevel budget,

        @JsonPropertyDescription("Preferred travel pace: RELAXED for fewer activities, MODERATE for balanced, INTENSIVE for maximum sightseeing")
        TravelPace pace,

        @JsonPropertyDescription("Dietary restrictions such as vegetarian, vegan, gluten-free, halal, kosher, etc.")
        List<String> dietaryRestrictions,

        @JsonPropertyDescription("Mobility constraints or physical limitations, empty string if none")
        String mobilityConstraints,

        @JsonPropertyDescription("Preferred language for the itinerary output, e.g. 'es' for Spanish, 'en' for English")
        String language
) {
}
