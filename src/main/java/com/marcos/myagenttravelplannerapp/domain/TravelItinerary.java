package com.marcos.myagenttravelplannerapp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.util.List;

public record TravelItinerary(
        @JsonPropertyDescription("Original travel request from the user")
        TravelRequest travelRequest,

        @JsonPropertyDescription("Profile of the destination including climate, currency, and safety info")
        DestinationProfile destinationProfile,

        @JsonPropertyDescription("Cultural insights including traditions, customs, and local phrases")
        CulturalInsights culturalInsights,

        @JsonPropertyDescription("Gastronomy guide with dishes, restaurants, and food experiences")
        GastronomyGuide gastronomyGuide,

        @JsonPropertyDescription("Day-by-day itinerary plans")
        List<DayPlan> dailyPlans,

        @JsonPropertyDescription("Practical travel tips for the destination")
        List<String> practicalTips,

        @JsonPropertyDescription("Recommended packing list based on destination and activities")
        List<String> packingList,

        @JsonPropertyDescription("Estimated total budget for the entire trip")
        String estimatedTotalBudget,

        @JsonPropertyDescription("Brief summary of the overall itinerary")
        String summary
) {
}
