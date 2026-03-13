package com.marcos.myagenttravelplannerapp.agent;

import com.embabel.agent.core.AgentPlatform;
import com.embabel.agent.core.AgentProcess;
import com.embabel.agent.core.AgentProcessStatusCode;
import com.embabel.agent.core.ProcessOptions;
import com.embabel.agent.domain.io.UserInput;
import com.marcos.myagenttravelplannerapp.domain.TravelItinerary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
class TravelPlannerAgentE2ETest {

    @Autowired
    private AgentPlatform platform;

    @Test
    @Timeout(value = 120, unit = TimeUnit.SECONDS)
    void generatesCompleteItineraryFromNaturalLanguage() {
        var agent = platform.agents().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No agents registered on the platform"));
        var input = new UserInput(
                "I want to go to Tokyo from April 15 to April 22, 2026. " +
                "I love gastronomy, history, and temples. Medium budget, moderate pace."
        );

        AgentProcess process = platform.runAgentFrom(agent, ProcessOptions.DEFAULT, Map.of("userInput", input));

        assertEquals(AgentProcessStatusCode.COMPLETED, process.getStatus());
        TravelItinerary itinerary = process.resultOfType(TravelItinerary.class);
        assertNotNull(itinerary);
        assertFalse(itinerary.summary().isBlank());
        assertFalse(itinerary.dailyPlans().isEmpty());
        assertEquals(8, itinerary.travelRequest().numberOfDays());
    }

    @Test
    @Timeout(value = 120, unit = TimeUnit.SECONDS)
    void generatesItineraryRespectingDietaryAndMobilityConstraints() {
        var agent = platform.agents().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No agents registered on the platform"));
        var input = new UserInput(
                "I'm traveling to Rome for 5 days in June 2026. " +
                "I'm vegetarian with reduced mobility. High budget, relaxed pace. I love art and music."
        );

        AgentProcess process = platform.runAgentFrom(agent, ProcessOptions.DEFAULT, Map.of("userInput", input));

        assertEquals(AgentProcessStatusCode.COMPLETED, process.getStatus());
        TravelItinerary itinerary = process.resultOfType(TravelItinerary.class);
        assertNotNull(itinerary);
        assertFalse(itinerary.dailyPlans().isEmpty());
        assertFalse(itinerary.practicalTips().isEmpty());
    }
}