package com.marcos.myagenttravelplannerapp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record Dish(
        @JsonPropertyDescription("Name of the dish")
        String name,

        @JsonPropertyDescription("Description of the dish, its ingredients and flavors")
        String description,

        @JsonPropertyDescription("Typical price range for this dish")
        String typicalPrice,

        @JsonPropertyDescription("Recommended place to try this dish")
        String whereToTry
) {
}
