package com.marcos.myagenttravelplannerapp.domain;

import org.junit.jupiter.api.Test;
import java.util.List;
import static com.marcos.myagenttravelplannerapp.fixtures.ItineraryFixtures.aClimateInfo;
import static com.marcos.myagenttravelplannerapp.fixtures.ItineraryFixtures.aDestinationProfile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DestinationProfileTest {

    @Test
    void createsProfileWithAllFields() {
        DestinationProfile profile = aDestinationProfile();

        assertEquals("Tokyo", profile.name());
        assertEquals("Japan", profile.country());
        assertEquals("Capital of Japan", profile.description());
        assertEquals("March-May", profile.bestTimeToVisit());
        assertEquals("Japanese Yen (JPY)", profile.localCurrency());
        assertFalse(profile.safetyNotes().isBlank());
    }

    @Test
    void climateInfoFieldsAreCorrect() {
        ClimateInfo climate = aClimateInfo();

        assertEquals("Mild spring", climate.expectedWeather());
        assertEquals("15-20°C", climate.averageTemperature());
        assertFalse(climate.packingRecommendations().isEmpty());
    }

    @Test
    void profileContainsExpectedLanguages() {
        DestinationProfile profile = aDestinationProfile();

        assertEquals(List.of("Japanese", "English"), profile.languages());
    }

    @Test
    void equalityIsBasedOnFieldValues() {
        DestinationProfile a = aDestinationProfile();
        DestinationProfile b = aDestinationProfile();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void climateInfoEqualityIsBasedOnFieldValues() {
        ClimateInfo a = aClimateInfo();
        ClimateInfo b = aClimateInfo();

        assertEquals(a, b);
    }
}