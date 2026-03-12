package com.marcos.myagenttravelplannerapp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record TravelRequest(
        @JsonPropertyDescription("Destination city or region the user wants to visit")
        String destination,

        @JsonPropertyDescription("Trip start date in ISO format (YYYY-MM-DD)")
        LocalDate startDate,

        @JsonPropertyDescription("Trip end date in ISO format (YYYY-MM-DD)")
        LocalDate endDate,

        @JsonPropertyDescription("Traveler preferences including interests, budget, pace, and restrictions")
        TravelerPreferences preferences
) {
    public TravelRequest {
        if (destination == null || destination.isBlank()) {
            throw new IllegalArgumentException("Destination must not be blank");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date must not be null");
        }
        if (!endDate.isAfter(startDate) && !endDate.isEqual(startDate)) {
            throw new IllegalArgumentException("End date must be on or after start date");
        }
    }

    public long numberOfDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}
