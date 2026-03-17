package com.marcos.myagenttravelplannerapp.agent;

import com.embabel.agent.api.common.StuckHandlerResult;
import com.embabel.agent.api.common.StuckHandlingResultCode;
import com.embabel.agent.core.AgentProcess;
import com.embabel.agent.domain.io.UserInput;
import com.embabel.agent.spi.ContextRepository;
import com.embabel.agent.test.unit.FakeOperationContext;
import com.marcos.myagenttravelplannerapp.domain.CulturalInsights;
import com.marcos.myagenttravelplannerapp.domain.DestinationProfile;
import com.marcos.myagenttravelplannerapp.domain.GastronomyGuide;
import com.marcos.myagenttravelplannerapp.domain.TravelItinerary;
import com.marcos.myagenttravelplannerapp.domain.TravelRequest;
import com.marcos.myagenttravelplannerapp.domain.TravelerPreferences;
import com.marcos.myagenttravelplannerapp.domain.TravelerProfile;
import com.marcos.myagenttravelplannerapp.domain.BudgetLevel;
import com.marcos.myagenttravelplannerapp.domain.TravelPace;
import com.marcos.myagenttravelplannerapp.memory.TravelerMemoryProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDate;
import java.util.List;
import static com.marcos.myagenttravelplannerapp.fixtures.ItineraryFixtures.aCulturalInsights;
import static com.marcos.myagenttravelplannerapp.fixtures.ItineraryFixtures.aDestinationProfile;
import static com.marcos.myagenttravelplannerapp.fixtures.ItineraryFixtures.aGastronomyGuide;
import static com.marcos.myagenttravelplannerapp.fixtures.ItineraryFixtures.aTravelItinerary;
import static com.marcos.myagenttravelplannerapp.fixtures.TravelRequestFixtures.aTravelRequest;
import static com.marcos.myagenttravelplannerapp.fixtures.TravelerProfileFixtures.anEmptyProfile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TravelPlannerAgentUnitTest {

    private TravelPlannerAgent agent;

    private static final TravelerMemoryProperties MEMORY_PROPS =
            new TravelerMemoryProperties(true, "default", "~/.travel-planner", 20, 3);

    private static final TravelerProfile EMPTY_PROFILE = anEmptyProfile();
    private static final TravelRequest REQUEST = aTravelRequest();
    private static final DestinationProfile PROFILE = aDestinationProfile();
    private static final CulturalInsights CULTURE = aCulturalInsights();
    private static final GastronomyGuide GASTRONOMY = aGastronomyGuide();

    @BeforeEach
    void setUp() {
        ContextRepository mockRepo = Mockito.mock(ContextRepository.class);
        agent = new TravelPlannerAgent(mockRepo, MEMORY_PROPS);
    }

    @Test
    void parseRequestReturnsTravelRequest() {
        var context = FakeOperationContext.create();
        context.expectResponse(REQUEST);

        TravelRequest result = agent.parseRequest(
                new UserInput("I want to go to Tokyo from April 15 to 22"),
                EMPTY_PROFILE,
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

        agent.parseRequest(new UserInput("Tokyo trip from April 15 to 22, love history"), EMPTY_PROFILE, context);

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
        var itinerary = aTravelItinerary();
        var context = FakeOperationContext.create();
        context.expectResponse(itinerary);

        TravelItinerary result = agent.buildItinerary(REQUEST, PROFILE, CULTURE, GASTRONOMY, EMPTY_PROFILE, context);

        assertNotNull(result);
        assertEquals(REQUEST, result.travelRequest());
        assertEquals(1, result.dailyPlans().size());
        assertEquals(1, context.getLlmInvocations().size());
    }

    @Test
    void buildItineraryPromptContainsNumberOfDays() {
        var context = FakeOperationContext.create();
        context.expectResponse(aTravelItinerary());

        agent.buildItinerary(REQUEST, PROFILE, CULTURE, GASTRONOMY, EMPTY_PROFILE, context);

        String prompt = context.getLlmInvocations().getFirst().getPrompt();
        assertTrue(prompt.contains("8 days"));
    }

    @Test
    void buildItineraryPromptContainsPaceAndBudget() {
        var context = FakeOperationContext.create();
        context.expectResponse(aTravelItinerary());

        agent.buildItinerary(REQUEST, PROFILE, CULTURE, GASTRONOMY, EMPTY_PROFILE, context);

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
}