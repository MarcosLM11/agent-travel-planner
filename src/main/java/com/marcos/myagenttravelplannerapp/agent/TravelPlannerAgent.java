package com.marcos.myagenttravelplannerapp.agent;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.OperationContext;
import com.embabel.agent.api.common.StuckHandler;
import com.embabel.agent.api.common.StuckHandlerResult;
import com.embabel.agent.api.common.StuckHandlingResultCode;
import com.embabel.agent.core.AgentProcess;
import com.embabel.agent.core.Context;
import com.embabel.agent.domain.io.UserInput;
import com.embabel.agent.spi.ContextRepository;
import com.marcos.myagenttravelplannerapp.domain.CulturalInsights;
import com.marcos.myagenttravelplannerapp.domain.DestinationProfile;
import com.marcos.myagenttravelplannerapp.domain.GastronomyGuide;
import com.marcos.myagenttravelplannerapp.domain.TravelItinerary;
import com.marcos.myagenttravelplannerapp.domain.TravelRequest;
import com.marcos.myagenttravelplannerapp.domain.TravelerProfile;
import com.marcos.myagenttravelplannerapp.memory.TravelerMemoryProperties;
import org.jetbrains.annotations.NotNull;

@Agent(description = "Generates personalized travel itineraries based on destination, dates, and traveler preferences")
public class TravelPlannerAgent implements StuckHandler {

    private final ContextRepository contextRepository;
    private final TravelerMemoryProperties memoryProperties;

    public TravelPlannerAgent(ContextRepository contextRepository, TravelerMemoryProperties memoryProperties) {
        this.contextRepository = contextRepository;
        this.memoryProperties = memoryProperties;
    }

    @Action(description = "Load persisted traveler profile from memory, or return empty profile for new users")
    public TravelerProfile loadTravelerProfile(UserInput input, OperationContext context) {
        String contextId = "user:" + memoryProperties.userId();
        Context ctx = contextRepository.findById(contextId);
        if (ctx == null) {
            return TravelerProfile.empty(memoryProperties.userId());
        }
        TravelerProfile profile = ctx.last(TravelerProfile.class);
        return profile != null ? profile : TravelerProfile.empty(memoryProperties.userId());
    }

    @Action(description = "Extract destination, dates, duration, and preferences from user's free-text input, enriched with profile memory")
    public TravelRequest parseRequest(UserInput input, TravelerProfile profile, OperationContext context) {
        return context.ai()
                .withDefaultLlm()
                .createObject(
                        "You are an expert travel planning assistant. " +
                        "Extract structured travel information from the following user input.\n" +
                        "Identify: destination (city or region), start date, end date, " +
                        "and preferences (interests, budget level [LOW/MEDIUM/HIGH/LUXURY], " +
                        "travel pace [RELAXED/MODERATE/INTENSIVE], dietary restrictions, " +
                        "mobility constraints, preferred output language).\n" +
                        "Defaults: budget=MEDIUM, pace=MODERATE, language inferred from user message or 'en'.\n" +
                        buildProfileContext(profile) +
                        "IMPORTANT: Return null if destination or dates are missing or ambiguous.\n" +
                        "User input: " + input.getContent(),
                        TravelRequest.class
                );
    }

    @Action(description = "Generate a profile of the destination including climate, currency, languages, and safety info")
    public DestinationProfile profileDestination(TravelRequest request, OperationContext context) {
        return context.ai()
                .withDefaultLlm()
                .createObject(
                        "You are a destination expert. Generate a comprehensive profile for: " + request.destination() + ".\n" +
                        "Travel period: " + request.startDate() + " to " + request.endDate() +
                        " (" + request.numberOfDays() + " days).\n" +
                        "Include:\n" +
                        "- Climate and expected weather for this specific travel period\n" +
                        "- Average temperature range and packing recommendations\n" +
                        "- Local currency and typical payment methods\n" +
                        "- Official and commonly spoken languages\n" +
                        "- Best time to visit (general annual recommendation)\n" +
                        "- Safety notes and areas or situations to be aware of",
                        DestinationProfile.class
                );
    }

    @Action(description = "Research local traditions, customs, etiquette, useful phrases, and events during travel dates")
    public CulturalInsights researchCulture(TravelRequest request, DestinationProfile profile, OperationContext context) {
        return context.ai()
                .withDefaultLlm()
                .createObject(
                        "You are a cultural expert on " + profile.name() + ", " + profile.country() + ".\n" +
                        "Research cultural insights for a traveler visiting from " +
                        request.startDate() + " to " + request.endDate() + ".\n" +
                        "Traveler interests: " + request.preferences().interests() + ".\n" +
                        "Provide:\n" +
                        "- At least 3 important local traditions with their cultural relevance\n" +
                        "- At least 3 key customs with specific do's and don'ts for tourists\n" +
                        "- Essential etiquette notes (dining, religious sites, social interactions)\n" +
                        "- 8-10 useful local phrases with translation and usage context\n" +
                        "- Festivals or events occurring between " + request.startDate() +
                        " and " + request.endDate() + " (empty list if none)",
                        CulturalInsights.class
                );
    }

    @Action(description = "Research local gastronomy including must-try dishes, restaurants, markets, and food experiences")
    public GastronomyGuide researchGastronomy(TravelRequest request, DestinationProfile profile, OperationContext context) {
        String dietaryNote = request.preferences().dietaryRestrictions().isEmpty()
                ? ""
                : "\nDietary restrictions: " + request.preferences().dietaryRestrictions() +
                  ". Filter ALL recommendations to respect these restrictions.";

        return context.ai()
                .withDefaultLlm()
                .createObject(
                        "You are a gastronomy expert on " + profile.name() + ", " + profile.country() + ".\n" +
                        "Budget level: " + request.preferences().budget() + "." + dietaryNote + "\n" +
                        "Provide:\n" +
                        "- At least 5 must-try local dishes with description, typical price, and where to find them\n" +
                        "- At least 4 recommended restaurants across different neighborhoods and price ranges\n" +
                        "- Notable food markets and street food areas with visiting tips\n" +
                        "- 2-3 food experiences (cooking classes, food tours, tastings, etc.)",
                        GastronomyGuide.class
                );
    }

    @AchievesGoal(description = "Generate a complete personalized travel itinerary")
    @Action(description = "Build a day-by-day itinerary integrating destination profile, culture, gastronomy, and traveler history")
    public TravelItinerary buildItinerary(
            TravelRequest request,
            DestinationProfile profile,
            CulturalInsights culture,
            GastronomyGuide gastronomy,
            TravelerProfile travelerProfile,
            OperationContext context) {

        String restrictions = "";
        if (!request.preferences().dietaryRestrictions().isEmpty()) {
            restrictions += ", dietary restrictions=" + request.preferences().dietaryRestrictions();
        }
        if (!request.preferences().mobilityConstraints().isBlank()) {
            restrictions += ", mobility constraints=" + request.preferences().mobilityConstraints();
        }

        String historyContext = buildHistoryContext(travelerProfile);

        return context.ai()
                .withDefaultLlm()
                .createObject(
                        "You are a professional travel planner. Build a complete " +
                        request.numberOfDays() + " days itinerary for " + request.destination() +
                        " from " + request.startDate() + " to " + request.endDate() + ".\n" +
                        "Traveler profile: interests=" + request.preferences().interests() +
                        ", pace=" + request.preferences().pace() +
                        ", budget=" + request.preferences().budget() + restrictions + ".\n" +
                        historyContext +
                        "Destination profile: " + profile + ".\n" +
                        "Cultural insights: " + culture + ".\n" +
                        "Gastronomy recommendations: " + gastronomy + ".\n" +
                        "Requirements:\n" +
                        "- Create exactly " + request.numberOfDays() + " DayPlan entries starting from " + request.startDate() + "\n" +
                        "- Each day: at least 2 morning activities, 2 afternoon activities, 1 evening activity\n" +
                        "- Each day: meal suggestions for breakfast, lunch, and dinner\n" +
                        "- Adapt activity intensity to travel pace: " + request.preferences().pace() + "\n" +
                        "- Include practical tips list and complete packing list\n" +
                        "- Provide estimated total budget in local currency\n" +
                        "- Output language: " + request.preferences().language(),
                        TravelItinerary.class
                );
    }

    @NotNull
    @Override
    public StuckHandlerResult handleStuck(AgentProcess process) {
        boolean hasTravelRequest = process.getObjects().stream()
                .anyMatch(TravelRequest.class::isInstance);

        if (!hasTravelRequest) {
            return new StuckHandlerResult(
                    "Cannot generate itinerary: travel request is missing or incomplete. " +
                    "Please provide a valid destination and travel dates.",
                    this,
                    StuckHandlingResultCode.NO_RESOLUTION,
                    process
            );
        }

        return new StuckHandlerResult(
                "Unable to complete the travel itinerary with the available information. " +
                "Please verify the destination and travel dates are valid.",
                this,
                StuckHandlingResultCode.NO_RESOLUTION,
                process
        );
    }

    private String buildProfileContext(TravelerProfile profile) {
        if (profile.tripHistory().isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder("Known traveler preferences from history:\n");
        if (profile.preferredBudget() != null) {
            sb.append("- Typical budget: ").append(profile.preferredBudget()).append("\n");
        }
        if (profile.preferredPace() != null) {
            sb.append("- Typical pace: ").append(profile.preferredPace()).append("\n");
        }
        if (!profile.dietaryRestrictions().isEmpty()) {
            sb.append("- Dietary restrictions: ").append(profile.dietaryRestrictions()).append("\n");
        }
        if (!profile.mobilityConstraints().isBlank()) {
            sb.append("- Mobility constraints: ").append(profile.mobilityConstraints()).append("\n");
        }
        sb.append("Use these as defaults when not explicitly specified in the current message.\n");
        return sb.toString();
    }

    private String buildHistoryContext(TravelerProfile profile) {
        if (profile.tripHistory().isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder("Traveler history:\n");
        if (!profile.visitedDestinations().isEmpty()) {
            sb.append("- Already visited: ").append(profile.visitedDestinations())
              .append(" — avoid repeating the same recommendations.\n");
        }
        if (!profile.cumulativeInterests().isEmpty()) {
            sb.append("- Recurring interests: ").append(profile.cumulativeInterests())
              .append(" — emphasize these in the itinerary.\n");
        }
        return sb.toString();
    }
}