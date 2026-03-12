package com.marcos.myagenttravelplannerapp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.util.List;

public record DestinationProfile(
        @JsonPropertyDescription("Name of the destination city or region")
        String name,

        @JsonPropertyDescription("Country where the destination is located")
        String country,

        @JsonPropertyDescription("Brief description of the destination and what makes it special")
        String description,

        @JsonPropertyDescription("Climate information for the travel dates")
        ClimateInfo climate,

        @JsonPropertyDescription("Best time of year to visit this destination")
        String bestTimeToVisit,

        @JsonPropertyDescription("Local currency name and code, e.g. 'Japanese Yen (JPY)'")
        String localCurrency,

        @JsonPropertyDescription("Languages spoken at the destination")
        List<String> languages,

        @JsonPropertyDescription("Safety notes and travel advisories for the destination")
        String safetyNotes
) {
}
