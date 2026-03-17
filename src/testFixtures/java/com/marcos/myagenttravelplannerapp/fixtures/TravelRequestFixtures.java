package com.marcos.myagenttravelplannerapp.fixtures;

import com.marcos.myagenttravelplannerapp.domain.BudgetLevel;
import com.marcos.myagenttravelplannerapp.domain.TravelPace;
import com.marcos.myagenttravelplannerapp.domain.TravelRequest;
import com.marcos.myagenttravelplannerapp.domain.TravelerPreferences;
import java.time.LocalDate;
import java.util.List;

public final class TravelRequestFixtures {

    public static final String DESTINATION = "Tokyo";
    public static final LocalDate START_DATE = LocalDate.of(2026, 4, 15);
    public static final LocalDate END_DATE = LocalDate.of(2026, 4, 22);

    private TravelRequestFixtures() {}

    public static TravelerPreferences aTravelerPreferences() {
        return new TravelerPreferences(
                List.of("history", "gastronomy"),
                BudgetLevel.MEDIUM,
                TravelPace.MODERATE,
                List.of(),
                "",
                "es"
        );
    }

    public static TravelRequest aTravelRequest() {
        return new TravelRequest(DESTINATION, START_DATE, END_DATE, aTravelerPreferences());
    }

    public static TravelRequest aMinimalTravelRequest() {
        return new TravelRequest(
                DESTINATION, START_DATE, END_DATE,
                new TravelerPreferences(List.of(), null, null, List.of(), "", "en")
        );
    }
}