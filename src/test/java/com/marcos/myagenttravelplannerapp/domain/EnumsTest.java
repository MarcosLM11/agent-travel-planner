package com.marcos.myagenttravelplannerapp.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnumsTest {

    @Test
    void budgetLevelHasFourValues() {
        assertEquals(4, BudgetLevel.values().length);
        assertNotNull(BudgetLevel.valueOf("LOW"));
        assertNotNull(BudgetLevel.valueOf("MEDIUM"));
        assertNotNull(BudgetLevel.valueOf("HIGH"));
        assertNotNull(BudgetLevel.valueOf("LUXURY"));
    }

    @Test
    void travelPaceHasThreeValues() {
        assertEquals(3, TravelPace.values().length);
        assertNotNull(TravelPace.valueOf("RELAXED"));
        assertNotNull(TravelPace.valueOf("MODERATE"));
        assertNotNull(TravelPace.valueOf("INTENSIVE"));
    }
}
