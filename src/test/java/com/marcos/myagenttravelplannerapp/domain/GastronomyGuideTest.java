package com.marcos.myagenttravelplannerapp.domain;

import org.junit.jupiter.api.Test;
import java.util.List;
import static com.marcos.myagenttravelplannerapp.fixtures.ItineraryFixtures.aGastronomyGuide;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GastronomyGuideTest {

    @Test
    void createsGuideWithAllNestedTypes() {
        GastronomyGuide guide = aGastronomyGuide();

        assertFalse(guide.mustTryDishes().isEmpty());
        assertFalse(guide.recommendedRestaurants().isEmpty());
        assertFalse(guide.foodMarkets().isEmpty());
        assertFalse(guide.foodExperiences().isEmpty());
    }

    @Test
    void dishFieldsAreCorrect() {
        Dish dish = aGastronomyGuide().mustTryDishes().getFirst();

        assertEquals("Ramen", dish.name());
        assertEquals("Noodle soup", dish.description());
        assertEquals("¥800-1200", dish.typicalPrice());
        assertEquals("Ichiran", dish.whereToTry());
    }

    @Test
    void restaurantFieldsAreCorrect() {
        Restaurant restaurant = aGastronomyGuide().recommendedRestaurants().getFirst();

        assertEquals("Ichiran", restaurant.name());
        assertEquals("Ramen", restaurant.cuisine());
        assertEquals("$$", restaurant.priceRange());
        assertEquals("Shibuya", restaurant.neighborhood());
    }

    @Test
    void pointOfInterestFieldsAreCorrect() {
        PointOfInterest poi = aGastronomyGuide().foodMarkets().getFirst();

        assertEquals("Tsukiji Outer Market", poi.name());
        assertEquals("market", poi.category());
        assertEquals("2 hours", poi.estimatedDuration());
        assertEquals("Free", poi.priceRange());
    }

    @Test
    void emptyGuideCanBeCreated() {
        GastronomyGuide guide = new GastronomyGuide(List.of(), List.of(), List.of(), List.of());

        assertEquals(0, guide.mustTryDishes().size());
        assertEquals(0, guide.recommendedRestaurants().size());
    }

    @Test
    void equalityIsBasedOnFieldValues() {
        GastronomyGuide a = aGastronomyGuide();
        GastronomyGuide b = aGastronomyGuide();

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }
}