package com.marcos.myagenttravelplannerapp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.util.List;

public record ClimateInfo(
        @JsonPropertyDescription("Expected weather conditions during the travel dates")
        String expectedWeather,

        @JsonPropertyDescription("Average temperature range during the travel dates, e.g. '15-22°C'")
        String averageTemperature,

        @JsonPropertyDescription("Recommended items to pack based on the expected weather")
        List<String> packingRecommendations
) {
}
