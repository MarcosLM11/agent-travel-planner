package com.marcos.myagenttravelplannerapp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record PointOfInterest(
        @JsonPropertyDescription("Name of the point of interest")
        String name,

        @JsonPropertyDescription("Category: museum, park, monument, market, temple, beach, etc.")
        String category,

        @JsonPropertyDescription("Description of the place and why it is worth visiting")
        String description,

        @JsonPropertyDescription("Recommended visit duration, e.g. '2 hours'")
        String estimatedDuration,

        @JsonPropertyDescription("Address or location")
        String address,

        @JsonPropertyDescription("Opening hours or availability")
        String openingHours,

        @JsonPropertyDescription("Entry fee or price range")
        String priceRange,

        @JsonPropertyDescription("Practical tips for visiting this place")
        String tips
) {
}
