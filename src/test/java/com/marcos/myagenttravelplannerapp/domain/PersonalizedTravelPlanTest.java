package com.marcos.myagenttravelplannerapp.domain;

import org.junit.jupiter.api.Test;
import static com.marcos.myagenttravelplannerapp.fixtures.ItineraryFixtures.aPersonalizedTravelPlan;
import static com.marcos.myagenttravelplannerapp.fixtures.ItineraryFixtures.aTravelItinerary;
import static com.marcos.myagenttravelplannerapp.fixtures.TravelerProfileFixtures.aRichProfile;
import static com.marcos.myagenttravelplannerapp.fixtures.TravelerProfileFixtures.anEmptyProfile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersonalizedTravelPlanTest {

    @Test
    void createsValidPlan() {
        PersonalizedTravelPlan plan = aPersonalizedTravelPlan();

        assertNotNull(plan.itinerary());
        assertNotNull(plan.updatedProfile());
    }

    @Test
    void planHoldsTheCorrectItinerary() {
        PersonalizedTravelPlan plan = aPersonalizedTravelPlan();

        assertEquals(aTravelItinerary(), plan.itinerary());
    }

    @Test
    void planHoldsTheCorrectProfile() {
        PersonalizedTravelPlan plan = aPersonalizedTravelPlan();

        assertEquals(aRichProfile(), plan.updatedProfile());
    }

    @Test
    void rejectsNullItinerary() {
        var emptyProfile = anEmptyProfile();
        assertThrows(NullPointerException.class,
                () -> new PersonalizedTravelPlan(null, emptyProfile));
    }

    @Test
    void rejectsNullUpdatedProfile() {
        var itinerary = aTravelItinerary();
        assertThrows(NullPointerException.class,
                () -> new PersonalizedTravelPlan(itinerary, null));
    }

    @Test
    void equalityIsBasedOnFieldValues() {
        PersonalizedTravelPlan a = aPersonalizedTravelPlan();
        PersonalizedTravelPlan b = aPersonalizedTravelPlan();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}