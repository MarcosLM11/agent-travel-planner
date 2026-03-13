package com.marcos.myagenttravelplannerapp.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record TravelerProfile(
        String userId,
        List<String> cumulativeInterests,
        BudgetLevel preferredBudget,
        TravelPace preferredPace,
        List<String> dietaryRestrictions,
        String mobilityConstraints,
        String preferredLanguage,
        List<String> visitedDestinations,
        List<TripSummary> tripHistory,
        LocalDateTime lastUpdated
) {
    public TravelerProfile {
        Objects.requireNonNull(userId, "userId must not be null");
        cumulativeInterests = cumulativeInterests != null ? List.copyOf(cumulativeInterests) : List.of();
        dietaryRestrictions = dietaryRestrictions != null ? List.copyOf(dietaryRestrictions) : List.of();
        mobilityConstraints = mobilityConstraints != null ? mobilityConstraints : "";
        preferredLanguage = preferredLanguage != null ? preferredLanguage : "en";
        visitedDestinations = visitedDestinations != null ? List.copyOf(visitedDestinations) : List.of();
        tripHistory = tripHistory != null ? List.copyOf(tripHistory) : List.of();
    }

    public static TravelerProfile empty(String userId) {
        return new TravelerProfile(userId, null, null, null, null, null, null, null, null, null);
    }
}