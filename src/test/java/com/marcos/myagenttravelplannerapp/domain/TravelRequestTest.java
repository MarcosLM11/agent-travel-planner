package com.marcos.myagenttravelplannerapp.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TravelRequestTest {

    private static final TravelerPreferences DEFAULT_PREFERENCES = new TravelerPreferences(
            List.of("history", "gastronomy"),
            BudgetLevel.MEDIUM,
            TravelPace.MODERATE,
            List.of(),
            "",
            "es"
    );

    @Test
    void numberOfDaysIsComputedFromDates() {
        var request = new TravelRequest(
                "Tokyo",
                LocalDate.of(2026, 4, 15),
                LocalDate.of(2026, 4, 22),
                DEFAULT_PREFERENCES
        );

        assertEquals(8, request.numberOfDays());
    }

    @Test
    void singleDayTripReturnsDayCountOfOne() {
        var request = new TravelRequest(
                "Madrid",
                LocalDate.of(2026, 6, 1),
                LocalDate.of(2026, 6, 1),
                DEFAULT_PREFERENCES
        );

        assertEquals(1, request.numberOfDays());
    }

    @Test
    void rejectsBlankDestination() {
        assertThrows(IllegalArgumentException.class, () ->
                new TravelRequest(
                        "  ",
                        LocalDate.of(2026, 4, 15),
                        LocalDate.of(2026, 4, 22),
                        DEFAULT_PREFERENCES
                )
        );
    }

    @Test
    void rejectsNullDestination() {
        assertThrows(IllegalArgumentException.class, () ->
                new TravelRequest(
                        null,
                        LocalDate.of(2026, 4, 15),
                        LocalDate.of(2026, 4, 22),
                        DEFAULT_PREFERENCES
                )
        );
    }

    @Test
    void rejectsNullStartDate() {
        assertThrows(IllegalArgumentException.class, () ->
                new TravelRequest(
                        "Tokyo",
                        null,
                        LocalDate.of(2026, 4, 22),
                        DEFAULT_PREFERENCES
                )
        );
    }

    @Test
    void rejectsNullEndDate() {
        assertThrows(IllegalArgumentException.class, () ->
                new TravelRequest(
                        "Tokyo",
                        LocalDate.of(2026, 4, 15),
                        null,
                        DEFAULT_PREFERENCES
                )
        );
    }

    @Test
    void rejectsEndDateBeforeStartDate() {
        assertThrows(IllegalArgumentException.class, () ->
                new TravelRequest(
                        "Tokyo",
                        LocalDate.of(2026, 4, 22),
                        LocalDate.of(2026, 4, 15),
                        DEFAULT_PREFERENCES
                )
        );
    }

    @Test
    void recordAccessorsReturnCorrectValues() {
        var start = LocalDate.of(2026, 7, 1);
        var end = LocalDate.of(2026, 7, 10);
        var request = new TravelRequest("Rome", start, end, DEFAULT_PREFERENCES);

        assertEquals("Rome", request.destination());
        assertEquals(start, request.startDate());
        assertEquals(end, request.endDate());
        assertSame(DEFAULT_PREFERENCES, request.preferences());
    }

    @Test
    void equalityIsBasedOnFieldValues() {
        var a = new TravelRequest("Paris", LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 5), DEFAULT_PREFERENCES);
        var b = new TravelRequest("Paris", LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 5), DEFAULT_PREFERENCES);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
