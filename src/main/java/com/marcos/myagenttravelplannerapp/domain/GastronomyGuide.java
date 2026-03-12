package com.marcos.myagenttravelplannerapp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.util.List;

public record GastronomyGuide(
        @JsonPropertyDescription("Must-try local dishes at the destination")
        List<Dish> mustTryDishes,

        @JsonPropertyDescription("Recommended restaurants across different price ranges")
        List<Restaurant> recommendedRestaurants,

        @JsonPropertyDescription("Food markets and street food spots worth visiting")
        List<PointOfInterest> foodMarkets,

        @JsonPropertyDescription("Food experiences such as cooking classes, food tours, tastings")
        List<String> foodExperiences
) {
}
