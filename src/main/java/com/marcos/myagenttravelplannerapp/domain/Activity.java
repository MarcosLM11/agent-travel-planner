package com.marcos.myagenttravelplannerapp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record Activity(
        @JsonPropertyDescription("Suggested time for this activity, e.g. '09:00'")
        String time,

        @JsonPropertyDescription("Name of the activity")
        String name,

        @JsonPropertyDescription("Description of what to do and what to expect")
        String description,

        @JsonPropertyDescription("Location or address of the activity")
        String location,

        @JsonPropertyDescription("Estimated duration, e.g. '1.5 hours'")
        String duration,

        @JsonPropertyDescription("Practical tips for this activity")
        String tips
) {
}
