package com.marcos.myagenttravelplannerapp.domain;

import org.junit.jupiter.api.Test;
import java.util.List;
import static com.marcos.myagenttravelplannerapp.fixtures.TravelerProfileFixtures.anEmptyProfile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TravelerProfileTest {

    @Test
    void emptyProfileHasCorrectUserId() {
        TravelerProfile profile = TravelerProfile.empty("Marcos");

        assertEquals("Marcos", profile.userId());
    }

    @Test
    void emptyProfileHasEmptyLists() {
        TravelerProfile profile = anEmptyProfile();

        assertTrue(profile.cumulativeInterests().isEmpty());
        assertTrue(profile.dietaryRestrictions().isEmpty());
        assertTrue(profile.visitedDestinations().isEmpty());
        assertTrue(profile.tripHistory().isEmpty());
    }

    @Test
    void emptyProfileHasNullEnums() {
        TravelerProfile profile = anEmptyProfile();

        assertNull(profile.preferredBudget());
        assertNull(profile.preferredPace());
    }

    @Test
    void nullListsAreNormalizedToEmpty() {
        TravelerProfile profile = new TravelerProfile(
                "default", null, null, null, null, null, null, null, null, null
        );

        assertNotNull(profile.cumulativeInterests());
        assertNotNull(profile.dietaryRestrictions());
        assertNotNull(profile.visitedDestinations());
        assertNotNull(profile.tripHistory());
        assertTrue(profile.cumulativeInterests().isEmpty());
    }

    @Test
    void nullUserIdThrowsException() {
        List<TripSummary> emptyTripSummaryList = List.of();
        List<String> empty = List.of();
        assertThrows(NullPointerException.class, () ->
                new TravelerProfile(null, empty, null, null, empty, "", "en", empty, emptyTripSummaryList, null)
        );
    }

    @Test
    void profileListsAreImmutable() {
        TravelerProfile profile = anEmptyProfile();
        List<String> interests = profile.cumulativeInterests();
        assertThrows(UnsupportedOperationException.class, () -> interests.add("history"));
    }
}