package com.marcos.myagenttravelplannerapp.domain;

import org.junit.jupiter.api.Test;
import java.util.List;
import static com.marcos.myagenttravelplannerapp.fixtures.ItineraryFixtures.aCulturalInsights;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CulturalInsightsTest {

    @Test
    void createsInsightsWithAllNestedTypes() {
        CulturalInsights insights = aCulturalInsights();

        assertFalse(insights.traditions().isEmpty());
        assertFalse(insights.customs().isEmpty());
        assertFalse(insights.etiquetteNotes().isEmpty());
        assertFalse(insights.localPhrases().isEmpty());
        assertFalse(insights.festivalsAndEvents().isEmpty());
    }

    @Test
    void traditionFieldsAreCorrect() {
        Tradition tradition = aCulturalInsights().traditions().getFirst();

        assertEquals("Hanami", tradition.name());
        assertEquals("Cherry blossom viewing", tradition.description());
        assertEquals("Spring tradition", tradition.relevance());
    }

    @Test
    void customFieldsAreCorrect() {
        Custom custom = aCulturalInsights().customs().getFirst();

        assertEquals("Bowing", custom.name());
        assertEquals("Greeting by bowing", custom.description());
        assertEquals("Bow when greeting", custom.doAndDont());
    }

    @Test
    void localPhraseFieldsAreCorrect() {
        LocalPhrase phrase = aCulturalInsights().localPhrases().getFirst();

        assertEquals("Arigatou", phrase.phrase());
        assertEquals("Thank you", phrase.translation());
        assertEquals("Use frequently", phrase.context());
    }

    @Test
    void eventFieldsAreCorrect() {
        Event event = aCulturalInsights().festivalsAndEvents().getFirst();

        assertEquals("Cherry Blossom Festival", event.name());
        assertEquals("Ueno Park", event.location());
    }

    @Test
    void emptyInsightsCanBeCreated() {
        CulturalInsights insights = new CulturalInsights(
                List.of(), List.of(), List.of(), List.of(), List.of()
        );

        assertEquals(0, insights.traditions().size());
        assertEquals(0, insights.customs().size());
    }

    @Test
    void equalityIsBasedOnFieldValues() {
        CulturalInsights a = aCulturalInsights();
        CulturalInsights b = aCulturalInsights();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}