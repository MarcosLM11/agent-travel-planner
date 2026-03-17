package com.marcos.myagenttravelplannerapp.domain;

import org.junit.jupiter.api.Test;
import java.util.List;
import static com.marcos.myagenttravelplannerapp.fixtures.ItineraryFixtures.aDayPlan;
import static com.marcos.myagenttravelplannerapp.fixtures.ItineraryFixtures.aMealPlan;
import static com.marcos.myagenttravelplannerapp.fixtures.ItineraryFixtures.aTravelItinerary;
import static com.marcos.myagenttravelplannerapp.fixtures.TravelRequestFixtures.aTravelRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TravelItineraryTest {

    @Test
    void createsItineraryWithAllFields() {
        TravelItinerary itinerary = aTravelItinerary();

        assertNotNull(itinerary.travelRequest());
        assertNotNull(itinerary.destinationProfile());
        assertNotNull(itinerary.culturalInsights());
        assertNotNull(itinerary.gastronomyGuide());
        assertFalse(itinerary.dailyPlans().isEmpty());
        assertFalse(itinerary.practicalTips().isEmpty());
        assertFalse(itinerary.packingList().isEmpty());
        assertFalse(itinerary.summary().isBlank());
    }

    @Test
    void dailyPlansCountMatchesTripDuration() {
        TravelItinerary itinerary = aTravelItinerary();

        assertEquals(1, itinerary.dailyPlans().size());
    }

    @Test
    void dayPlanHasCorrectStructure() {
        DayPlan day = aDayPlan();

        assertEquals(1, day.dayNumber());
        assertEquals("Arrival day", day.theme());
        assertFalse(day.morning().isEmpty());
        assertFalse(day.afternoon().isEmpty());
        assertFalse(day.evening().isEmpty());
        assertNotNull(day.meals());
        assertFalse(day.transportNotes().isBlank());
        assertFalse(day.estimatedBudget().isBlank());
    }

    @Test
    void mealPlanContainsAllMeals() {
        MealPlan meals = aMealPlan();

        assertNotNull(meals.breakfast());
        assertNotNull(meals.lunch());
        assertNotNull(meals.dinner());
    }

    @Test
    void mealSuggestionFieldsAreCorrect() {
        MealPlan meals = aMealPlan();

        assertEquals("Hotel", meals.breakfast().restaurant());
        assertEquals("Japanese set", meals.breakfast().dish());
        assertEquals("$$", meals.breakfast().priceRange());
    }

    @Test
    void activityFieldsAreCorrect() {
        DayPlan day = aDayPlan();
        Activity activity = day.morning().getFirst();

        assertEquals("10:00", activity.time());
        assertEquals("Sensoji", activity.name());
        assertEquals("Asakusa", activity.location());
        assertEquals("2h", activity.duration());
    }

    @Test
    void itineraryLinksToOriginalRequest() {
        TravelItinerary itinerary = aTravelItinerary();

        assertEquals(aTravelRequest(), itinerary.travelRequest());
    }

    @Test
    void itineraryWithEmptyPackingList() {
        TravelItinerary itinerary = new TravelItinerary(
                aTravelRequest(), null, null, null,
                List.of(), List.of(), List.of(), "¥0", "summary"
        );

        assertEquals(0, itinerary.packingList().size());
    }

    @Test
    void equalityIsBasedOnFieldValues() {
        TravelItinerary a = aTravelItinerary();
        TravelItinerary b = aTravelItinerary();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}