package com.marcos.myagenttravelplannerapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "OPENAI_API_KEY", matches = ".+")
class MyAgentTravelPlannerAppApplicationTests {

    @Test
    void contextLoads() {
    }

}
