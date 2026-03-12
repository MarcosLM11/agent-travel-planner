package com.marcos.myagenttravelplannerapp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record Event(
        @JsonPropertyDescription("Name of the festival or event")
        String name,

        @JsonPropertyDescription("Date or date range of the event")
        String date,

        @JsonPropertyDescription("Description of the event and what to expect")
        String description,

        @JsonPropertyDescription("Location or venue where the event takes place")
        String location
) {
}
