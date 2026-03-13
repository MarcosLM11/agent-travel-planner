package com.marcos.myagenttravelplannerapp.agent;

import com.embabel.agent.api.common.StuckHandlerResult;
import com.embabel.agent.api.common.StuckHandlingResultCode;
import com.embabel.agent.core.AgentProcess;
import com.embabel.agent.domain.io.UserInput;
import com.embabel.agent.test.unit.FakeOperationContext;
import com.marcos.myagenttravelplannerapp.domain.Activity;
import com.marcos.myagenttravelplannerapp.domain.BudgetLevel;
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
import com.marcos.myagenttravelplannerapp.domain.PointOfInterest;
import com.marcos.myagenttravelplannerapp.domain.Restaurant;
import com.marcos.myagenttravelplannerapp.domain.Tradition;
import com.marcos.myagenttravelplannerapp.domain.TravelItinerary;
import com.marcos.myagenttravelplannerapp.domain.TravelPace;
import com.marcos.myagenttravelplannerapp.domain.TravelRequest;
import com.marcos.myagenttravelplannerapp.domain.TravelerPreferences;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TravelPlannerAgentUnitTest {

    private TravelPlannerAgent agent;

    private static final TravelerPreferences PREFS = new TravelerPreferences(
            List.of("history", "gastronomy"),
            BudgetLevel.MEDIUM,
            TravelPace.MODERATE,
            List.of(),
            "",
            "es"
    );

    private static final TravelRequest REQUEST = new TravelRequest(
            "Tokyo",
            LocalDate.of(2026, 4, 15),
            LocalDate.of(2026, 4, 22),
            PREFS
    );

    private static final DestinationProfile PROFILE = new DestinationProfile(
            "Tokyo", "Japan", "Capital of Japan",
            new ClimateInfo("Mild spring", "15-20°C", List.of("Light jacket", "Umbrella")),
            "March-May", "Japanese Yen (JPY)", List.of("Japanese", "English"),
            "Very safe city"
    );

    private static final CulturalInsights CULTURE = new CulturalInsights(
            List.of(new Tradition("Hanami", "Cherry blossom viewing", "Spring tradition")),
            List.of(new Custom("Bowing", "Greeting by bowing", "Bow when greeting")),
            List.of("Remove shoes indoors"),
            List.of(new LocalPhrase("Arigatou", "Thank you", "Use frequently")),
            List.of(new Event("Cherry Blossom Festival", "April 2026", "Annual festival", "Ueno Park"))
    );

    private static final GastronomyGuide GASTRONOMY = new GastronomyGuide(
            List.of(new Dish("Ramen", "Noodle soup", "¥800-1200", "Ichiran")),
            List.of(new Restaurant("Ichiran", "Ramen", "$$", "Shibuya", "No reservations needed")),
            List.of(new PointOfInterest("Tsukiji Outer Market", "market", "Fish market", "2 hours", "Tsukiji", "6am-2pm", "Free", "Go early")),
            List.of("Sushi-making class")
    );

    @BeforeEach
    void setUp() {
        agent = new TravelPlannerAgent();
    }

    @Test
    void parseRequestReturnsTravelRequest() {
        var context = FakeOperationContext.create();
        context.expectResponse(REQUEST);

        TravelRequest result = agent.parseRequest(
                new UserInput("I want to go to Tokyo from April 15 to 22"),
                context
        );

        assertEquals("Tokyo", result.destination());
        assertEquals(8, result.numberOfDays());
        assertEquals(1, context.getLlmInvocations().size());
        assertTrue(context.getLlmInvocations().getFirst().getPrompt().contains("Tokyo"));
    }

    @Test
    void parseRequestPromptContainsUserInput() {
        var context = FakeOperationContext.create();
        context.expectResponse(REQUEST);

        agent.parseRequest(new UserInput("Tokyo trip from April 15 to 22, love history"), context);

        String prompt = context.getLlmInvocations().getFirst().getPrompt();
        assertTrue(prompt.contains("Tokyo trip from April 15 to 22, love history"));
    }

    @Test
    void profileDestinationReturnsProfile() {
        var context = FakeOperationContext.create();
        context.expectResponse(PROFILE);

        DestinationProfile result = agent.profileDestination(REQUEST, context);

        assertEquals("Tokyo", result.name());
        assertEquals("Japan", result.country());
        assertEquals(1, context.getLlmInvocations().size());
    }

    @Test
    void profileDestinationPromptContainsDatesAndDestination() {
        var context = FakeOperationContext.create();
        context.expectResponse(PROFILE);

        agent.profileDestination(REQUEST, context);

        String prompt = context.getLlmInvocations().getFirst().getPrompt();
        assertTrue(prompt.contains("Tokyo"));
        assertTrue(prompt.contains("2026-04-15"));
        assertTrue(prompt.contains("2026-04-22"));
    }

    @Test
    void researchCultureReturnsCulturalInsights() {
        var context = FakeOperationContext.create();
        context.expectResponse(CULTURE);

        CulturalInsights result = agent.researchCulture(REQUEST, PROFILE, context);

        assertFalse(result.traditions().isEmpty());
        assertFalse(result.customs().isEmpty());
        assertEquals(1, context.getLlmInvocations().size());
    }

    @Test
    void researchCulturePromptContainsInterests() {
        var context = FakeOperationContext.create();
        context.expectResponse(CULTURE);

        agent.researchCulture(REQUEST, PROFILE, context);

        String prompt = context.getLlmInvocations().getFirst().getPrompt();
        assertTrue(prompt.contains("history"));
        assertTrue(prompt.contains("gastronomy"));
    }

    @Test
    void researchGastronomyReturnsGuide() {
        var context = FakeOperationContext.create();
        context.expectResponse(GASTRONOMY);

        GastronomyGuide result = agent.researchGastronomy(REQUEST, PROFILE, context);

        assertFalse(result.mustTryDishes().isEmpty());
        assertEquals(1, context.getLlmInvocations().size());
    }

    @Test
    void researchGastronomyPromptIncludesDietaryRestrictions() {
        var prefsWithDiet = new TravelerPreferences(
                List.of("gastronomy"), BudgetLevel.HIGH, TravelPace.RELAXED,
                List.of("vegetarian", "gluten-free"), "", "en"
        );
        var requestWithDiet = new TravelRequest(
                "Tokyo", LocalDate.of(2026, 4, 15), LocalDate.of(2026, 4, 22), prefsWithDiet
        );
        var context = FakeOperationContext.create();
        context.expectResponse(GASTRONOMY);

        agent.researchGastronomy(requestWithDiet, PROFILE, context);

        String prompt = context.getLlmInvocations().getFirst().getPrompt();
        assertTrue(prompt.contains("vegetarian"));
        assertTrue(prompt.contains("gluten-free"));
    }

    @Test
    void researchGastronomyPromptOmitsDietaryNoteWhenEmpty() {
        var context = FakeOperationContext.create();
        context.expectResponse(GASTRONOMY);

        agent.researchGastronomy(REQUEST, PROFILE, context);

        String prompt = context.getLlmInvocations().getFirst().getPrompt();
        assertFalse(prompt.contains("dietary restrictions:"));
    }

    @Test
    void buildItineraryProducesItinerary() {
        var itinerary = buildSampleItinerary();
        var context = FakeOperationContext.create();
        context.expectResponse(itinerary);

        TravelItinerary result = agent.buildItinerary(REQUEST, PROFILE, CULTURE, GASTRONOMY, context);

        assertNotNull(result);
        assertEquals(REQUEST, result.travelRequest());
        assertEquals(1, result.dailyPlans().size());
        assertEquals(1, context.getLlmInvocations().size());
    }

    @Test
    void buildItineraryPromptContainsNumberOfDays() {
        var context = FakeOperationContext.create();
        context.expectResponse(buildSampleItinerary());

        agent.buildItinerary(REQUEST, PROFILE, CULTURE, GASTRONOMY, context);

        String prompt = context.getLlmInvocations().getFirst().getPrompt();
        assertTrue(prompt.contains("8 days"));
    }

    @Test
    void buildItineraryPromptContainsPaceAndBudget() {
        var context = FakeOperationContext.create();
        context.expectResponse(buildSampleItinerary());

        agent.buildItinerary(REQUEST, PROFILE, CULTURE, GASTRONOMY, context);

        String prompt = context.getLlmInvocations().getFirst().getPrompt();
        assertTrue(prompt.contains("MODERATE"));
        assertTrue(prompt.contains("MEDIUM"));
    }

    @Test
    void handleStuckWithoutTravelRequestReturnsNoResolution() {
        AgentProcess process = Mockito.mock(AgentProcess.class);
        Mockito.when(process.getObjects()).thenReturn(List.of());

        StuckHandlerResult result = agent.handleStuck(process);

        assertEquals(StuckHandlingResultCode.NO_RESOLUTION, result.getCode());
        assertTrue(result.getMessage().toLowerCase().contains("travel request"));
    }

    @Test
    void handleStuckWithTravelRequestReturnsNoResolution() {
        AgentProcess process = Mockito.mock(AgentProcess.class);
        Mockito.when(process.getObjects()).thenReturn(List.of(REQUEST));

        StuckHandlerResult result = agent.handleStuck(process);

        assertEquals(StuckHandlingResultCode.NO_RESOLUTION, result.getCode());
    }

    private TravelItinerary buildSampleItinerary() {
        return new TravelItinerary(
                REQUEST, PROFILE, CULTURE, GASTRONOMY,
                List.of(new DayPlan(
                        1, LocalDate.of(2026, 4, 15), "Arrival day",
                        List.of(new Activity("10:00", "Sensoji", "Ancient temple", "Asakusa", "2h", "Go early")),
                        List.of(new Activity("14:00", "Meiji Shrine", "Shrine", "Harajuku", "1.5h", "Free")),
                        List.of(new Activity("19:00", "Shibuya", "Crossing", "Shibuya", "1h", "Best at night")),
                        new MealPlan(
                                new MealSuggestion("Hotel", "Japanese set", "Western", "$$"),
                                new MealSuggestion("Ichiran", "Ramen", "Udon", "$$"),
                                new MealSuggestion("Gonpachi", "Yakitori", "Tempura", "$$$")
                        ),
                        "Use Suica card", "¥15000"
                )),
                List.of("Carry cash", "Get a Suica card"),
                List.of("Light jacket", "Comfortable shoes"),
                "¥120000",
                "8-day exploration of Tokyo"
        );
    }
}
