package com.marcos.myagenttravelplannerapp.agent;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.domain.io.UserInput;
import com.marcos.myagenttravelplannerapp.domain.DestinationProfile;
import com.marcos.myagenttravelplannerapp.domain.GastronomyGuide;
import com.marcos.myagenttravelplannerapp.domain.TravelItinerary;
import com.marcos.myagenttravelplannerapp.domain.TravelRequest;
import com.marcos.myagenttravelplannerapp.domain.CulturalInsights;

@Agent(description = "Generates personalized travel itineraries based on destination, dates, and traveler preferences")
public class TravelPlannerAgent {

    @Action(description = "Extract destination, dates, duration, and preferences from user's free-text input")
    public TravelRequest parseRequest(UserInput input, OperationContext context) {
        return context.ai()
                .withDefaultLlm()
                .createObject(
                        "Extract the travel request from the following user input. " +
                        "Identify the destination, start date, end date, and traveler preferences " +
                        "(interests, budget level, travel pace, dietary restrictions, mobility constraints, preferred language). " +
                        "If budget is not specified, default to MEDIUM. If pace is not specified, default to MODERATE. " +
                        "User input: " + input.getContent(),
                        TravelRequest.class
                );
    }

    @Action(description = "Generate a profile of the destination including climate, currency, languages, and safety info")
    public DestinationProfile profileDestination(TravelRequest request, OperationContext context) {
        return context.ai()
                .withDefaultLlm()
                .createObject(
                        "Generate a detailed profile for the destination: " + request.destination() + ". " +
                        "Include climate information for the travel dates (" + request.startDate() + " to " + request.endDate() + "), " +
                        "local currency, spoken languages, best time to visit, and safety notes.",
                        DestinationProfile.class
                );
    }

    @Action(description = "Research local traditions, customs, etiquette, useful phrases, and events during travel dates")
    public CulturalInsights researchCulture(TravelRequest request, DestinationProfile profile, OperationContext context) {
        return context.ai()
                .withDefaultLlm()
                .createObject(
                        "Research the culture of " + profile.name() + ", " + profile.country() + " for a traveler visiting from " +
                        request.startDate() + " to " + request.endDate() + ". " +
                        "Include local traditions, customs with do's and don'ts, etiquette notes, " +
                        "useful local phrases with translations, and any festivals or events happening during those dates. " +
                        "The traveler is interested in: " + request.preferences().interests() + ".",
                        CulturalInsights.class
                );
    }

    @Action(description = "Research local gastronomy including must-try dishes, restaurants, markets, and food experiences")
    public GastronomyGuide researchGastronomy(TravelRequest request, DestinationProfile profile, OperationContext context) {
        String dietaryNote = request.preferences().dietaryRestrictions().isEmpty()
                ? ""
                : " The traveler has dietary restrictions: " + request.preferences().dietaryRestrictions() + ".";

        return context.ai()
                .withDefaultLlm()
                .createObject(
                        "Research the gastronomy of " + profile.name() + ", " + profile.country() + ". " +
                        "Include must-try local dishes, recommended restaurants across different price ranges, " +
                        "food markets, and food experiences like cooking classes or food tours. " +
                        "Budget level: " + request.preferences().budget() + "." + dietaryNote,
                        GastronomyGuide.class
                );
    }

    @AchievesGoal(description = "Generate a complete personalized travel itinerary")
    @Action(description = "Build a day-by-day itinerary integrating destination profile, culture, and gastronomy")
    public TravelItinerary buildItinerary(
            TravelRequest request,
            DestinationProfile profile,
            CulturalInsights culture,
            GastronomyGuide gastronomy,
            OperationContext context) {

        return context.ai()
                .withDefaultLlm()
                .createObject(
                        "Build a complete day-by-day travel itinerary for " + request.destination() +
                        " from " + request.startDate() + " to " + request.endDate() +
                        " (" + request.numberOfDays() + " days). " +
                        "Traveler preferences: interests=" + request.preferences().interests() +
                        ", pace=" + request.preferences().pace() +
                        ", budget=" + request.preferences().budget() + ". " +
                        "Use the following destination profile: " + profile + ". " +
                        "Integrate cultural insights: " + culture + ". " +
                        "Integrate gastronomy recommendations: " + gastronomy + ". " +
                        "Each day must have morning, afternoon, and evening activities, plus meal suggestions. " +
                        "Include practical tips, a packing list, and an estimated total budget. " +
                        "Output language: " + request.preferences().language() + ".",
                        TravelItinerary.class
                );
    }
}
