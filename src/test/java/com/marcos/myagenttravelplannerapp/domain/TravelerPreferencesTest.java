package com.marcos.myagenttravelplannerapp.domain;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TravelerPreferencesTest {

    @Test
    void createsPreferencesWithAllFields() {
        var prefs = new TravelerPreferences(
                List.of("history", "nature", "art"),
                BudgetLevel.HIGH,
                TravelPace.RELAXED,
                List.of("vegetarian", "gluten-free"),
                "wheelchair access needed",
                "en"
        );

        assertEquals(3, prefs.interests().size());
        assertEquals(BudgetLevel.HIGH, prefs.budget());
        assertEquals(TravelPace.RELAXED, prefs.pace());
        assertEquals(2, prefs.dietaryRestrictions().size());
        assertEquals("wheelchair access needed", prefs.mobilityConstraints());
        assertEquals("en", prefs.language());
    }

    @Test
    void acceptsEmptyListsAndConstraints() {
        var prefs = new TravelerPreferences(
                List.of(),
                BudgetLevel.LOW,
                TravelPace.INTENSIVE,
                List.of(),
                "",
                "es"
        );

        assertTrue(prefs.interests().isEmpty());
        assertTrue(prefs.dietaryRestrictions().isEmpty());
        assertTrue(prefs.mobilityConstraints().isEmpty());
    }

    @Test
    void equalityIsBasedOnFieldValues() {
        var a = new TravelerPreferences(List.of("art"), BudgetLevel.MEDIUM, TravelPace.MODERATE, List.of(), "", "es");
        var b = new TravelerPreferences(List.of("art"), BudgetLevel.MEDIUM, TravelPace.MODERATE, List.of(), "", "es");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
