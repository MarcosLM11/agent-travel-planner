# Travel Planner Agent

> An AI-powered travel itinerary generator built with **Java 25**, **Spring Boot 3.5**, and the **Embabel Agent Framework**. Describe your trip in plain language and receive a fully personalized, day-by-day itinerary with cultural insights, gastronomy recommendations, and smart memory of your past trips.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
  - [Agent Flow (GOAP)](#agent-flow-goap)
  - [Domain Model](#domain-model)
  - [Traveler Memory](#traveler-memory)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Configuration](#configuration)
  - [Build](#build)
  - [Run](#run)
- [Usage](#usage)
- [Testing](#testing)
- [Roadmap](#roadmap)
- [Glossary](#glossary)

---

## Overview

**Travel Planner Agent** orchestrates multiple specialized AI actions that collaborate to generate comprehensive travel plans. The user provides a destination, dates, and personal preferences in free-text form — the system does the rest.

**Key capabilities:**

- Natural language input → structured itinerary
- Day-by-day plans with morning / afternoon / evening activities
- Cultural insights: traditions, customs, etiquette, local phrases, and festivals
- Gastronomy guide: must-try dishes, restaurants, markets, and food experiences
- Respects dietary restrictions, mobility constraints, budget, and travel pace
- Persistent traveler memory: learns preferences across trips and avoids repeating past destinations
- Resilient planning: automatic replanning via GOAP and a `StuckHandler` for unrecoverable states

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 3.5 (SNAPSHOT) |
| Agent Framework | [Embabel Agent Framework](https://github.com/embabel/embabel-agent) 0.3.4 |
| Planning algorithm | GOAP (Goal-Oriented Action Planning with A\*) |
| LLM providers | OpenAI (default: `gpt-4.1-nano`) · Anthropic Claude |
| Persistence | SQLite via Spring Data JPA + Hibernate Community Dialects |
| Build tool | Maven Wrapper (`mvnw`) — no local Maven needed |
| Testing | JUnit 5 · Embabel Test Support · ArchUnit |
| Shell UI | Embabel Interactive Shell Starter |

---

## Architecture

### Agent Flow (GOAP)

The **Embabel GOAP planner** automatically infers the optimal action sequence from the type signatures of each `@Action` method. No explicit wiring is required.

```
UserInput  (free text from the shell)
    │
    ▼
loadTravelerProfile()    ──▶  TravelerProfile   (from SQLite memory)
    │
    ▼
parseRequest()           ──▶  TravelRequest     (destination, dates, preferences)
    │
    ▼
profileDestination()     ──▶  DestinationProfile (climate, currency, safety…)
    │
    ├────────────────────────────────┐
    ▼                                ▼
researchCulture()            researchGastronomy()
    │                                │
    ▼                                ▼
CulturalInsights             GastronomyGuide
    │                                │
    └──────────────┬─────────────────┘
                   ▼
          buildItinerary()   ──▶  TravelItinerary  ✓ GOAL
```

`researchCulture` and `researchGastronomy` are independent of each other — the GOAP planner may schedule them in any order it deems optimal.

### Domain Model

All domain types are immutable Java **records**, validated in compact constructors.

```
TravelRequest
  ├── destination, startDate, endDate, numberOfDays
  └── TravelerPreferences
        ├── interests: List<String>
        ├── budget:    BudgetLevel   (LOW | MEDIUM | HIGH | LUXURY)
        ├── pace:      TravelPace    (RELAXED | MODERATE | INTENSIVE)
        ├── dietaryRestrictions, mobilityConstraints, language

DestinationProfile
  └── ClimateInfo (weather, temperature, packing list)

CulturalInsights
  ├── Tradition, Custom, LocalPhrase
  └── Event (festivals during travel dates)

GastronomyGuide
  ├── Dish, Restaurant
  └── PointOfInterest (food markets)

TravelItinerary  ← GOAL
  └── List<DayPlan>
        └── morning/afternoon/evening: List<Activity>
            MealPlan (breakfast/lunch/dinner: MealSuggestion)
```

### Traveler Memory

A `TravelerProfile` is persisted in SQLite after each trip and loaded at the start of every new request. The profile accumulates:

- **Visited destinations** — avoids repeating the same recommendations
- **Recurring interests** — emphasised in future itineraries
- **Preferred budget, pace, dietary restrictions** — used as defaults when not explicitly stated

Memory is configured under `travel-planner.memory` in `application.yaml`.

---

## Project Structure

```
my-agent-travel-planner-app/
├── doc/
│   └── SPEC.md                          # Full functional specification
├── src/
│   ├── main/
│   │   ├── java/com/marcos/myagenttravelplannerapp/
│   │   │   ├── MyAgentTravelPlannerAppApplication.java   # @SpringBootApplication @EnableAgents
│   │   │   ├── agent/
│   │   │   │   └── TravelPlannerAgent.java               # All @Action methods + StuckHandler
│   │   │   ├── domain/                                   # Immutable records
│   │   │   │   ├── TravelRequest.java
│   │   │   │   ├── TravelerPreferences.java
│   │   │   │   ├── BudgetLevel.java  (enum)
│   │   │   │   ├── TravelPace.java   (enum)
│   │   │   │   ├── DestinationProfile.java / ClimateInfo.java
│   │   │   │   ├── CulturalInsights.java
│   │   │   │   ├── GastronomyGuide.java
│   │   │   │   ├── DayPlan.java / Activity.java / MealPlan.java
│   │   │   │   ├── TravelItinerary.java                  # Goal object
│   │   │   │   ├── TravelerProfile.java                  # Persistent memory object
│   │   │   │   └── TripSummary.java
│   │   │   └── memory/
│   │   │       ├── SqliteContextRepository.java          # ContextRepository impl (SQLite)
│   │   │       ├── TravelerContextEntity.java
│   │   │       ├── TravelerContextJpaRepository.java
│   │   │       ├── SqliteStorageConfig.java
│   │   │       └── TravelerMemoryProperties.java
│   │   └── resources/
│   │       └── application.yaml
│   ├── test/
│   │   └── java/com/marcos/myagenttravelplannerapp/
│   │       ├── agent/
│   │       │   ├── TravelPlannerAgentUnitTest.java
│   │       │   ├── TravelPlannerAgentMemoryUnitTest.java
│   │       │   ├── TravelPlannerAgentIntegrationTest.java
│   │       │   └── TravelPlannerAgentE2ETest.java
│   │       ├── domain/                                   # Record validation tests
│   │       ├── memory/
│   │       │   └── SqliteContextRepositoryTest.java
│   │       └── architecture/
│   │           └── ArchitectureTest.java                 # ArchUnit layer rules
│   └── testFixtures/                                     # Shared test fixtures
├── .env.example
├── pom.xml
├── mvnw / mvnw.cmd
└── CLAUDE.md
```

---

## Getting Started

### Prerequisites

- **JDK 25** (e.g. [Temurin](https://adoptium.net/))
- At least one LLM API key: **OpenAI** or **Anthropic**

No local Maven installation is required — the project ships with `mvnw`.

### Configuration

Copy `.env.example` to `.env` and fill in your API key(s):

```bash
cp .env.example .env
```

```dotenv
# .env  — only one key is required; both can coexist
OPENAI_API_KEY=sk-...
ANTHROPIC_API_KEY=sk-ant-...
```

The active LLM model is configured in `src/main/resources/application.yaml`:

```yaml
embabel:
  models:
    default-llm: gpt-4.1-nano   # Change to any supported model

travel-planner:
  memory:
    enabled: true
    user-id: default
    storage-path: ./data        # SQLite database location
    max-trip-history: 20
```

### Build

```bash
./mvnw clean package
```

To skip tests during build:

```bash
./mvnw clean package -DskipTests
```

### Run

```bash
./mvnw spring-boot:run
```

The interactive shell starts automatically. You will see a prompt where you can type your travel request.

---

## Usage

Once the shell is running, describe your trip in natural language:

```
> I want to go to Tokyo from April 15 to 22. I love gastronomy, history, and temples.
  Medium budget. Moderate pace.
```

```
> Trip to Rome for 5 days in June. I'm vegetarian, limited mobility.
  High budget. Relaxed pace, focused on art and music.
```

```
> Valencia from March 15 to 20. I want to experience local festivals.
```

The agent will:
1. Parse your request and load your traveler profile from memory
2. Build a destination profile with climate and safety info
3. Research culture: traditions, etiquette, local phrases, and events during your dates
4. Research gastronomy: dishes, restaurants, and food markets (filtered by your dietary restrictions)
5. Build a complete day-by-day itinerary and display it in the shell

**If destination or dates are missing**, the agent returns a clarification message and requests the missing information before continuing.

---

## Testing

The project follows a **Spec-Driven Development** approach: tests are written before implementation, derived directly from the specification.

### Run all tests

```bash
./mvnw test
```

### Run a single test class

```bash
./mvnw test -Dtest=TravelPlannerAgentUnitTest
```

### Run a single test method

```bash
./mvnw test -Dtest=TravelPlannerAgentUnitTest#parseRequest_extractsDestinationAndDates
```

### Test levels

| Level | What is tested | LLM required | Tools used |
|---|---|---|---|
| **Unit** | Each `@Action` in isolation | No | `FakeOperationContext` |
| **Memory Unit** | `loadTravelerProfile` and profile enrichment | No | `FakeOperationContext` |
| **Integration** | Full agent flow from `UserInput` to `TravelItinerary` | No | `IntegrationTestUtils.dummyAgentPlatform()` |
| **E2E** | Real LLM call, validates output quality | Yes | `@EnabledIfEnvironmentVariable` |
| **Architecture** | Layer dependencies and package rules | No | ArchUnit |

### Key test scenarios

- `parseRequest` correctly extracts destination, dates, days count, and preferences from free text
- `parseRequest` returns `null` when destination or dates are missing (triggers GOAP replanning)
- `researchGastronomy` filters all recommendations to respect dietary restrictions
- `buildItinerary` generates exactly N `DayPlan` entries for an N-day trip
- Each `DayPlan` contains activities for morning, afternoon, and evening
- SQLite repository correctly serialises / deserialises `TravelerProfile`
- Architecture: `memory` and `domain` packages have no dependency on `agent`

---

## Roadmap

| Phase | Status | Description |
|---|---|---|
| 1 — Foundation | Done | Domain model, validation, unit tests |
| 2 — Core Agent | Done | `TravelPlannerAgent` with 5 actions + integration tests |
| 3 — Memory | Done | Persistent `TravelerProfile` via SQLite, `StuckHandler`, E2E tests |
| 4 — Extensions | Planned | Transport agent, accommodation agent, MCP Server interface |

---

## Glossary

| Term | Definition |
|---|---|
| **GOAP** | Goal-Oriented Action Planning. Uses an A\* search to find the optimal action sequence that achieves the goal from the current world state |
| **Blackboard** | Shared typed memory in Embabel where objects are placed and consumed between actions |
| **Action** | A method annotated with `@Action` that performs a discrete processing step |
| **Goal** | The terminal state the agent must reach, marked with `@AchievesGoal` |
| **Replanning** | GOAP recalculates the plan after each action or when an action returns `null` |
| **StuckHandler** | Fallback invoked when the planner cannot find any valid next action |
| **DICE** | Domain-Integrated Context Engineering — Embabel's principle where typed domain objects serve as the bridge between business logic and LLM context |
| **Spec-Driven Development** | Specification defines expected behaviour; tests are derived from the spec before implementation begins |