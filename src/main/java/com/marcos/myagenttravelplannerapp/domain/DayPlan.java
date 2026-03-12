package com.marcos.myagenttravelplannerapp.domain;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.time.LocalDate;
import java.util.List;

public record DayPlan(
        @JsonPropertyDescription("Day number in the itinerary, starting from 1")
        int dayNumber,

        @JsonPropertyDescription("Date for this day plan")
        LocalDate date,

        @JsonPropertyDescription("Theme or focus of the day, e.g. 'Historic center exploration', 'Gastronomy day'")
        String theme,

        @JsonPropertyDescription("Morning activities")
        List<Activity> morning,

        @JsonPropertyDescription("Afternoon activities")
        List<Activity> afternoon,

        @JsonPropertyDescription("Evening activities")
        List<Activity> evening,

        @JsonPropertyDescription("Meal suggestions for the day")
        MealPlan meals,

        @JsonPropertyDescription("Transport notes and recommendations for getting around this day")
        String transportNotes,

        @JsonPropertyDescription("Estimated budget for this day")
        String estimatedBudget
) {
}
