package com.marcos.myagenttravelplannerapp.domain;

import java.util.Objects;

public record PersonalizedTravelPlan(
        TravelItinerary itinerary,
        TravelerProfile updatedProfile
) {
    public PersonalizedTravelPlan {
        Objects.requireNonNull(itinerary, "itinerary must not be null");
        Objects.requireNonNull(updatedProfile, "updatedProfile must not be null");
    }
}