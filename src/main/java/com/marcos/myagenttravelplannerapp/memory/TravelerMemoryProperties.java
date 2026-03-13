package com.marcos.myagenttravelplannerapp.memory;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "travel-planner.memory")
public record TravelerMemoryProperties(
        boolean enabled,
        String userId,
        String storagePath,
        int maxTripHistory,
        int preferenceWindowSize
) {}