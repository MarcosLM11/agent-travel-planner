package com.marcos.myagenttravelplannerapp.fixtures;

import com.marcos.myagenttravelplannerapp.domain.BudgetLevel;
import com.marcos.myagenttravelplannerapp.domain.TravelPace;
import com.marcos.myagenttravelplannerapp.domain.TravelerProfile;
import com.marcos.myagenttravelplannerapp.domain.TripSummary;
import java.time.LocalDate;
import java.util.List;

public final class TravelerProfileFixtures {

    private TravelerProfileFixtures() {}

    public static TravelerProfile anEmptyProfile() {
        return TravelerProfile.empty("default");
    }

    public static TripSummary aTripSummary() {
        return new TripSummary(
                "trip-1", "Tokyo",
                LocalDate.of(2026, 4, 15), LocalDate.of(2026, 4, 22),
                List.of("history", "gastronomy"),
                BudgetLevel.MEDIUM, TravelPace.MODERATE, null
        );
    }

    public static TravelerProfile aRichProfile() {
        return new TravelerProfile(
                "default",
                List.of("history"),
                BudgetLevel.MEDIUM,
                TravelPace.RELAXED,
                List.of("vegetarian"),
                "",
                "en",
                List.of("Paris"),
                List.of(aTripSummary()),
                null
        );
    }
}