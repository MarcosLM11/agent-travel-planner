package com.marcos.myagenttravelplannerapp.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static com.marcos.myagenttravelplannerapp.fixtures.TravelRequestFixtures.aTravelerPreferences;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TravelRequestTest {

    private static final TravelerPreferences DEFAULT_PREFERENCES = aTravelerPreferences();

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
        var start = LocalDate.of(2026, 4, 15);
        var end = LocalDate.of(2026, 4, 22);
        assertThrows(IllegalArgumentException.class, () -> new TravelRequest("  ", start, end, DEFAULT_PREFERENCES));
    }

    @Test
    void rejectsNullDestination() {
        var start = LocalDate.of(2026, 4, 15);
        var end = LocalDate.of(2026, 4, 22);
        assertThrows(IllegalArgumentException.class, () -> new TravelRequest(null, start, end, DEFAULT_PREFERENCES));
    }

    @Test
    void rejectsNullStartDate() {
        var end = LocalDate.of(2026, 4, 22);
        assertThrows(IllegalArgumentException.class, () -> new TravelRequest("Tokyo", null, end, DEFAULT_PREFERENCES));
    }

    @Test
    void rejectsNullEndDate() {
        var start = LocalDate.of(2026, 4, 15);
        assertThrows(IllegalArgumentException.class, () -> new TravelRequest("Tokyo", start, null, DEFAULT_PREFERENCES));
    }

    @Test
    void rejectsEndDateBeforeStartDate() {
        var start = LocalDate.of(2026, 4, 22);
        var end = LocalDate.of(2026, 4, 15);
        assertThrows(IllegalArgumentException.class, () -> new TravelRequest("Tokyo", start, end, DEFAULT_PREFERENCES));
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
