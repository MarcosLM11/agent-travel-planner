package com.marcos.myagenttravelplannerapp.agent;

import com.embabel.agent.api.common.scope.AgentScopeBuilder;
import com.embabel.agent.core.AgentScope;
import com.embabel.agent.spi.ContextRepository;
import com.marcos.myagenttravelplannerapp.memory.TravelerMemoryProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TravelPlannerAgentIntegrationTest {

    private AgentScope agentScope;

    @BeforeEach
    void setUp() {
        ContextRepository mockRepo = Mockito.mock(ContextRepository.class);
        TravelerMemoryProperties props = new TravelerMemoryProperties(true, "default", "~/.travel-planner", 20, 3);
        agentScope = AgentScopeBuilder.fromInstance(new TravelPlannerAgent(mockRepo, props)).createAgentScope();
    }

    @Test
    void agentScopeHasSixActions() {
        assertEquals(6, agentScope.getActions().size());
    }

    @Test
    void agentScopeHasOneGoal() {
        assertEquals(1, agentScope.getGoals().size());
    }

    @Test
    void goalTargetsTravelItinerary() {
        var goal = agentScope.getGoals().iterator().next();
        assertNotNull(goal.getOutputType());
        assertTrue(goal.getOutputType().toString().contains("TravelItinerary"));
    }

    @Test
    void allActionsHaveDescriptions() {
        agentScope.getActions().forEach(action ->
                assertNotNull(action.getDescription(), "Action '" + action.getName() + "' must have a description")
        );
    }

    @Test
    void agentHasDescription() {
        assertFalse(agentScope.getDescription().isBlank());
    }
}