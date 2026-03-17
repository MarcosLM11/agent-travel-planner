package com.marcos.myagenttravelplannerapp.fixtures;

import com.marcos.myagenttravelplannerapp.domain.Activity;
import com.marcos.myagenttravelplannerapp.domain.ClimateInfo;
import com.marcos.myagenttravelplannerapp.domain.CulturalInsights;
import com.marcos.myagenttravelplannerapp.domain.Custom;
import com.marcos.myagenttravelplannerapp.domain.DayPlan;
import com.marcos.myagenttravelplannerapp.domain.DestinationProfile;
import com.marcos.myagenttravelplannerapp.domain.Dish;
import com.marcos.myagenttravelplannerapp.domain.Event;
import com.marcos.myagenttravelplannerapp.domain.GastronomyGuide;
import com.marcos.myagenttravelplannerapp.domain.LocalPhrase;
import com.marcos.myagenttravelplannerapp.domain.MealPlan;
import com.marcos.myagenttravelplannerapp.domain.MealSuggestion;
import com.marcos.myagenttravelplannerapp.domain.PersonalizedTravelPlan;
import com.marcos.myagenttravelplannerapp.domain.PointOfInterest;
import com.marcos.myagenttravelplannerapp.domain.Restaurant;
import com.marcos.myagenttravelplannerapp.domain.Tradition;
import com.marcos.myagenttravelplannerapp.domain.TravelItinerary;
import java.time.LocalDate;
import java.util.List;
import static com.marcos.myagenttravelplannerapp.fixtures.TravelRequestFixtures.aTravelRequest;
import static com.marcos.myagenttravelplannerapp.fixtures.TravelerProfileFixtures.aRichProfile;

public final class ItineraryFixtures {

    private ItineraryFixtures() {}

    public static ClimateInfo aClimateInfo() {
        return new ClimateInfo("Mild spring", "15-20°C", List.of("Light jacket", "Umbrella"));
    }

    public static DestinationProfile aDestinationProfile() {
        return new DestinationProfile(
                "Tokyo", "Japan", "Capital of Japan",
                aClimateInfo(),
                "March-May", "Japanese Yen (JPY)", List.of("Japanese", "English"),
                "Very safe city"
        );
    }

    public static CulturalInsights aCulturalInsights() {
        return new CulturalInsights(
                List.of(new Tradition("Hanami", "Cherry blossom viewing", "Spring tradition")),
                List.of(new Custom("Bowing", "Greeting by bowing", "Bow when greeting")),
                List.of("Remove shoes indoors"),
                List.of(new LocalPhrase("Arigatou", "Thank you", "Use frequently")),
                List.of(new Event("Cherry Blossom Festival", "April 2026", "Annual festival", "Ueno Park"))
        );
    }

    public static GastronomyGuide aGastronomyGuide() {
        return new GastronomyGuide(
                List.of(new Dish("Ramen", "Noodle soup", "¥800-1200", "Ichiran")),
                List.of(new Restaurant("Ichiran", "Ramen", "$$", "Shibuya", "No reservations needed")),
                List.of(new PointOfInterest("Tsukiji Outer Market", "market", "Fish market", "2 hours", "Tsukiji", "6am-2pm", "Free", "Go early")),
                List.of("Sushi-making class")
        );
    }

    public static Activity anActivity() {
        return new Activity("10:00", "Sensoji", "Ancient temple", "Asakusa", "2h", "Go early");
    }

    public static MealSuggestion aMealSuggestion() {
        return new MealSuggestion("Hotel", "Japanese set", "Western", "$$");
    }

    public static MealPlan aMealPlan() {
        return new MealPlan(
                new MealSuggestion("Hotel", "Japanese set", "Western", "$$"),
                new MealSuggestion("Ichiran", "Ramen", "Udon", "$$"),
                new MealSuggestion("Gonpachi", "Yakitori", "Tempura", "$$$")
        );
    }

    public static DayPlan aDayPlan() {
        return new DayPlan(
                1, LocalDate.of(2026, 4, 15), "Arrival day",
                List.of(new Activity("10:00", "Sensoji", "Ancient temple", "Asakusa", "2h", "Go early")),
                List.of(new Activity("14:00", "Meiji Shrine", "Shrine", "Harajuku", "1.5h", "Free")),
                List.of(new Activity("19:00", "Shibuya", "Crossing", "Shibuya", "1h", "Best at night")),
                aMealPlan(),
                "Use Suica card", "¥15000"
        );
    }

    public static TravelItinerary aTravelItinerary() {
        return new TravelItinerary(
                aTravelRequest(),
                aDestinationProfile(),
                aCulturalInsights(),
                aGastronomyGuide(),
                List.of(aDayPlan()),
                List.of("Carry cash", "Get a Suica card"),
                List.of("Light jacket", "Comfortable shoes"),
                "¥120000",
                "8-day exploration of Tokyo"
        );
    }

    public static PersonalizedTravelPlan aPersonalizedTravelPlan() {
        return new PersonalizedTravelPlan(aTravelItinerary(), aRichProfile());
    }
}