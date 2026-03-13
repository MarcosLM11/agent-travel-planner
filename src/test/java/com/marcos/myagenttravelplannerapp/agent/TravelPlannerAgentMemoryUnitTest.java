package com.marcos.myagenttravelplannerapp.agent;

import com.embabel.agent.core.Context;
import com.embabel.agent.domain.io.UserInput;
import com.embabel.agent.spi.ContextRepository;
import com.embabel.agent.spi.support.InMemoryContext;
import com.embabel.agent.test.unit.FakeOperationContext;
import com.marcos.myagenttravelplannerapp.domain.BudgetLevel;
import com.marcos.myagenttravelplannerapp.domain.TravelPace;
import com.marcos.myagenttravelplannerapp.domain.TravelItinerary;
import com.marcos.myagenttravelplannerapp.domain.TravelRequest;
import com.marcos.myagenttravelplannerapp.domain.TravelerPreferences;
import com.marcos.myagenttravelplannerapp.domain.TravelerProfile;
import com.marcos.myagenttravelplannerapp.domain.TripSummary;
import com.marcos.myagenttravelplannerapp.memory.TravelerMemoryProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TravelPlannerAgentMemoryUnitTest {

    private ContextRepository mockRepo;
    private TravelPlannerAgent agent;

    private static final TravelerMemoryProperties PROPS =
            new TravelerMemoryProperties(true, "default", "~/.travel-planner", 20, 3);

    @BeforeEach
    void setUp() {
        mockRepo = mock(ContextRepository.class);
        agent = new TravelPlannerAgent(mockRepo, PROPS);
    }

    @Test
    void loadTravelerProfile_newUser_returnsEmptyProfile() {
        when(mockRepo.findById("user:default")).thenReturn(null);

        TravelerProfile result = agent.loadTravelerProfile(new UserInput("test"), FakeOperationContext.create());

        assertNotNull(result);
        assertEquals("default", result.userId());
        assertTrue(result.tripHistory().isEmpty());
        assertNull(result.preferredBudget());
    }

    @Test
    void loadTravelerProfile_existingUser_returnsPersistedProfile() {
        TravelerProfile saved = new TravelerProfile(
                "default", List.of("history"), BudgetLevel.HIGH, TravelPace.RELAXED,
                List.of(), "", "en", List.of("Paris"), List.of(), null
        );
        Context ctx = new InMemoryContext("user:default");
        ctx.addObject(saved);
        when(mockRepo.findById("user:default")).thenReturn(ctx);

        TravelerProfile result = agent.loadTravelerProfile(new UserInput("test"), FakeOperationContext.create());

        assertEquals("default", result.userId());
        assertEquals(List.of("history"), result.cumulativeInterests());
        assertEquals(BudgetLevel.HIGH, result.preferredBudget());
        assertEquals(List.of("Paris"), result.visitedDestinations());
    }

    @Test
    void loadTravelerProfile_contextWithoutProfile_returnsEmptyProfile() {
        // Context exists but has no TravelerProfile object in it
        Context emptyCtx = new InMemoryContext("user:default");
        when(mockRepo.findById("user:default")).thenReturn(emptyCtx);

        TravelerProfile result = agent.loadTravelerProfile(new UserInput("test"), FakeOperationContext.create());

        assertEquals("default", result.userId());
        assertTrue(result.tripHistory().isEmpty());
    }

    @Test
    void parseRequest_withEmptyProfile_promptHasNoProfileContext() {
        TravelRequest stubResponse = new TravelRequest(
                "Rome", LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 8),
                new TravelerPreferences(List.of(), BudgetLevel.MEDIUM, TravelPace.MODERATE, List.of(), "", "en")
        );
        var context = FakeOperationContext.create();
        context.expectResponse(stubResponse);

        agent.parseRequest(new UserInput("I want to visit Rome"), TravelerProfile.empty("default"), context);

        String prompt = context.getLlmInvocations().getFirst().getPrompt();
        assertTrue(prompt.contains("Rome"));
        assertFalse(prompt.contains("Known traveler preferences"));
    }

    @Test
    void parseRequest_withRichProfile_promptIncludesProfileContext() {
        TravelRequest stubResponse = new TravelRequest(
                "Rome", LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 8),
                new TravelerPreferences(List.of(), BudgetLevel.MEDIUM, TravelPace.MODERATE, List.of(), "", "en")
        );
        TravelerProfile profileWithTrips = new TravelerProfile(
                "default", List.of("art"), BudgetLevel.MEDIUM, TravelPace.RELAXED,
                List.of("vegetarian"), "", "en", List.of("Paris"),
                List.of(new TripSummary("t1", "Paris", null, null, List.of("art"), BudgetLevel.MEDIUM, TravelPace.RELAXED, null)),
                null
        );
        var context = FakeOperationContext.create();
        context.expectResponse(stubResponse);

        agent.parseRequest(new UserInput("I want to visit Rome"), profileWithTrips, context);

        String prompt = context.getLlmInvocations().getFirst().getPrompt();
        assertTrue(prompt.contains("Known traveler preferences"));
        assertTrue(prompt.contains("MEDIUM"));
        assertTrue(prompt.contains("vegetarian"));
    }

    @Test
    void buildItinerary_withVisitedDestinations_promptMentionsThem() {
        TravelerProfile travelerProfile = new TravelerProfile(
                "default", List.of("history"), BudgetLevel.MEDIUM, TravelPace.MODERATE,
                List.of(), "", "en", List.of("Rome", "Barcelona"),
                List.of(new TripSummary("t1", "Rome", null, null, List.of(), null, null, null)),
                null
        );
        TravelRequest request = new TravelRequest(
                "Tokyo", LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 7),
                new TravelerPreferences(List.of("history"), BudgetLevel.MEDIUM, TravelPace.MODERATE, List.of(), "", "en")
        );
        TravelItinerary stubItinerary =
                new TravelItinerary(
                        request, null, null, null, List.of(), List.of(), List.of(), "", "stub"
                );
        var context = FakeOperationContext.create();
        context.expectResponse(stubItinerary);

        agent.buildItinerary(request, null, null, null, travelerProfile, context);

        String prompt = context.getLlmInvocations().getFirst().getPrompt();
        assertTrue(prompt.contains("Rome"));
        assertTrue(prompt.contains("Barcelona"));
        assertTrue(prompt.contains("Already visited"));
    }
}