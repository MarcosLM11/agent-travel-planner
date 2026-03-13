package com.marcos.myagenttravelplannerapp.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TripSummaryTest {

    @Test
    void validTripSummaryCreation() {
        TripSummary summary = new TripSummary(
                "trip-001", "Tokyo",
                LocalDate.of(2026, 4, 15), LocalDate.of(2026, 4, 22),
                List.of("history", "gastronomy"),
                BudgetLevel.MEDIUM, TravelPace.MODERATE,
                LocalDateTime.now()
        );

        assertEquals("trip-001", summary.tripId());
        assertEquals("Tokyo", summary.destination());
        assertEquals(2, summary.interestsUsed().size());
    }

    @Test
    void nullTripIdThrowsException() {
        List<String> lista = List.of();
        assertThrows(NullPointerException.class, () ->
                new TripSummary(null, "Tokyo", null, null, lista, null, null, null)
        );
    }

    @Test
    void nullDestinationThrowsException() {
        List<String> lista = List.of();
        assertThrows(NullPointerException.class, () ->
                new TripSummary("id", null, null, null, lista, null, null, null)
        );
    }

    @Test
    void nullInterestsNormalizedToEmpty() {
        TripSummary summary = new TripSummary("id", "Tokyo", null, null, null, null, null, null);

        assertTrue(summary.interestsUsed().isEmpty());
    }
}