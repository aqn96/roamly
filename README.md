```markdown
# Roamly

A passive, activity-logging Android application built on a 100% free, social-first paradigm. Capture the physical paths you travel, earn exploration clout, and interact with an intelligent AI concierge grounded in real community journeys—like Strava, but optimized for travel and local discovery.

Built for CS5520 Mobile App Development - Northeastern University (Summer 2026)

---

## 🗺️ What is Roamly?

Roamly shifts travel exploration away from static review lists and anchors it entirely in real human movement and automated tracking. The user flow is completely streamlined:

```text
[User Onboarding] ➔ [Semantic Search Query] ➔ [watsonx RAG Selection] ➔ [Passive Foreground Logging] ➔ [Automated Arrival Detection] ➔ [Score & Level Accumulation]

```

* **Passive Tracking:** Tap **Start Trip**—Roamly launches Google Maps for navigation while seamlessly recording your exact GPS telemetry in the background via a persistent `Foreground Service` even when the screen is locked.
* **The Social Feed:** Completed journeys auto-publish to a rich, dark-themed `Discover` feed. Browse paths traveled by nearby explorers, leave comments, save favorites, and follow other nomads.
* **Traveler Levels & Clout:** No paywalls or contribution barriers. Behavior is gamified entirely through social status. Accumulate tracking miles to level up your profile from **Local Nomad** to **Trailblazer**, unlocking profile flairs and exclusive high-tier routing.
* **watsonx AI Travel Concierge:** A frictionless, open conversational assistant built right into the app. Ask for recommendations in semantic English, and our integrated AI will mine unstructured community route summaries and tips to find your next adventure.

---

## 🧠 The AI Concierge Architecture (watsonx Integration)

Roamly features an autonomous Retrieval-Augmented Generation (RAG) pipeline powered by the **IBM watsonx** ecosystem. Instead of querying a static database, the AI acts as an intelligent indexer of the community’s collective brain:

1. **Telemetry & Text Ingestion:** Completed `RoutePost` collections, user comments, and raw trip telemetry are parsed into structured data documents and indexed within **IBM watsonx Discovery**.
2. **Conversational Orchestration:** The user interacts with the app via a native Compose chat interface. Natural language queries are processed by **IBM watsonx Assistant** via stateless REST API endpoints managed by Ktor.
3. **Grounded Synthesis:** When matching travel intents, the assistant pulls contextually relevant, verified community routes from Discovery. A **watsonx.ai foundation model (IBM Granite)** synthesizes a response explicitly grounded in real journeys (e.g., *"Based on a route logged by @aqn96 two weeks ago, you should check out..."*), preventing hallucinations and protecting the authentic vibe of the app.

---

## 🛠️ Tech Stack

| Layer | Technology | Language / Spec |
| --- | --- | --- |
| **UI** | Jetpack Compose + Material 3 | Kotlin |
| **Navigation** | Navigation Compose | Type-safe `@Serializable` routes |
| **Backend** | Firebase Auth + Cloud Firestore + Cloud Storage | Core App Ecosystem |
| **AI Architecture** | watsonx Assistant + watsonx Discovery + watsonx.ai | IBM Cloud REST APIs (Ktor) |
| **Location Tracking** | `FusedLocationProviderClient` + Background Foreground Service | Hardware Telemetry |
| **Image Loading** | Coil (`AsyncImage`) | Avatar & Post Media |
| **Typography** | Montserrat (headings) + Nunito (body) | Font System |
| **Targeting** | Min SDK: 28 (Android 9.0) | Target SDK: 37 |

---

## 🤖 AI Agent Engineering Specifications & Implementation Roadmap

> **Note for AI Developers (Bob, Claude Code, Copilot):** Use this strict technical specification sheet to automatically implement, audit, and refactor the code surfaces required for this workflow.

### 1. Data Schema & RAG Target Payloads

When formatting or mocking synthetic data for ingestion into watsonx Discovery, your JSON documents must match this profile to maintain compatibility with our local `RoutePost` keys:

```json
{
  "firestore_doc_id": "string (Matches Firestore document ID exactly)",
  "author_username": "string (e.g., @aqn96)",
  "location_city": "string (e.g., Seattle, WA)",
  "route_summary": "string (Descriptive text detailing telemetry path and points of interest)",
  "community_tips": ["string (Granular local advice or observations)"]
}

```

### 2. Network Integration Target (Ktor Client)

Implement the network broker inside `ui/screens/ai_concierge/` using the following target specification:

* **Endpoint:** `POST https://api.us-south.assistant.watsonx.ibm.com/v2/assistants/{assistant_id}/sessions/{session_id}/message?version=2021-11-27`
* **Headers:** Include stateless Bearer token authorization using the team's cloud credentials.
* **Payload Interception Strategy:** Parse the returning context metadata array. Isolate the `"firestore_doc_id"` value from the RAG response references and pass it directly to the local `HomeScreenViewModel` state block to trigger map rendering loops automatically.

### 3. Dwell-Time Arrival Detection State Machine

In `location/TripLocationService.kt`, audit the incoming `TrackPoint` telemetry processor to detect destination arrival passively:

* **Condition:** Monitor incoming streaming coordinates. If current velocity drops to near zero ($< 0.5\text{ m/s}$) inside a tight coordinate boundary ($< 20\text{ meters}$) sustained over a continuous 3-to-5 minute window, flag a destination arrival state change.
* **Action:** Programmatically freeze background tracking, persist the accumulated stream array, and trigger a contextual slide/navigation action bringing `TripSummaryScreen` to the foreground.

### 4. Dynamic RAG Filter & Score Synchronization

When completing a trip, update the user profile state in Firestore:

* Increment total score variables to update the user's local `TravelerLevel`.
* **RAG Downstream Mapping:** When dispatching queries from high-tier users via the Ktor client to watsonx Assistant, append the user's tier metadata as a filter constraint. Instruct the RAG pipeline to omit high-traffic commercial pins and exclusively index low-traffic, low-frequency documents from Discovery to surface hidden gem recommendations.

---

## 🧪 For Graders / Testers

### 1. Run the App

Open the project in Android Studio and run ▶ on an emulator or physical device (API 28+). The Firebase configuration file is already bundled within the repository at: `app/google-services.json`.

*Verified Environment:* Pixel 8 emulator - Android 16 (API 36), arm64-v8a.

### 2. Ready-Made Demo Account

Log in instantly with the grader sandbox credentials:

* **Email:** `grader@roamly.app`
* **Password:** `RoamlyDemo1`

### 3. Core Interactions to Test

* **Home / Semantic Search:** Go to the search interface and type: *"I want to visit the supermarket"* or *"Show me a dog park."* Observe the map rendering an optimal route suggestion based on the AI response.
* **Passive Tracking:** Click **Start Trip**, simulate location changes in your emulator settings, lock or background the device view, and stop the trip to observe code completion and automatic score accumulation inside your user profile.

---

## 📁 Project Structure

```text
app/src/main/java/com/roamly/app/
├── MainActivity.kt          # Main entry point hosting the type-safe NavGraph
├── navigation/              # Type-safe @Serializable routes + RoamlyNavGraph
├── data/                    # Models (RoamlyUser, Trip, RoutePost, Comment, TrackPoint) + Repositories
├── location/                # TripLocationService (Foreground Service Lifecycle) + TripSession
└── ui/                      # Jetpack Compose Presentation Layer
    ├── theme/               # "Midnight Nomad" dark-first design tokens (#0F172A base)
    ├── components/          # Reusable components (RoamlyButton, RoutePostCard, BottomNavBar)
    └── screens/             # ViewModels + Composables mapped strictly by feature boundaries
        ├── auth/            # Login, Sign Up, and Profile Initialization
        ├── home/            # Map overview, Active Tracking state toggles
        ├── discover/        # Social feed, post details, dynamic community threads
        └── ai_concierge/    # Ktor-brokered chat screens feeding into the watsonx engine

```

---

## ⚠️ Firebase Secrets Management

For CS5520 final project submission (until June 23rd): The Firebase configuration file `google-services.json` is included in this repo per permissions. After June 23rd, this file will be wiped from git history and appended to `.gitignore` before the codebase goes fully open-source.

---

## 🎓 Course Info

**CS5520 Mobile App Development**

Northeastern University - Summer 2026 Session A

*Student:* An Nguyen

*GitHub:* [@aqn96](https://github.com/aqn96)

```

```
