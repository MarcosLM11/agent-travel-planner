package com.marcos.myagenttravelplannerapp.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record TripSummary(
        String tripId,
        String destination,
        LocalDate startDate,
        LocalDate endDate,
        List<String> interestsUsed,
        BudgetLevel budget,
        TravelPace pace,
        LocalDateTime plannedAt
) {
    public TripSummary {
        Objects.requireNonNull(tripId, "tripId must not be null");
        Objects.requireNonNull(destination, "destination must not be null");
        interestsUsed = interestsUsed != null ? List.copyOf(interestsUsed) : List.of();
    }
}