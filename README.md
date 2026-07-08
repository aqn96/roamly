```markdown
# Roamly

A passive travel route-logging Android app built on a 100% free, social-first paradigm. Capture the physical paths you travel, level up your explorer status, and interact with an intelligent AI concierge grounded in real community journeys—like Strava, but for travel discovery.

Built for CS5520 Mobile App Development - Northeastern University (Summer 2026)

---

## 🗺️ What is Roamly?

Roamly shifts travel exploration away from static, corporate "top-10" review lists and anchors it in real human movement and community connection. 

* **Passive Tracking:** Tap **Start Trip**—Roamly launches Google Maps for navigation while seamlessly recording your exact GPS telemetry in the background via a persistent `Foreground Service`.
* **The Social Feed:** Completed journeys auto-publish to a rich, dark-themed `Discover` feed. Browse paths traveled by nearby explorers, leave comments, save favorites, and follow other nomads.
* **Traveler Levels & Clout:** No paywalls or contribution barriers. Behavior is gamified entirely through social status. Accumulate tracking miles to level up your profile from **Local Nomad** to **Trailblazer**, unlocking exclusive custom profile badges.
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
| :--- | :--- | :--- |
| **UI** | Jetpack Compose + Material 3 | Kotlin |
| **Navigation** | Navigation Compose | Type-safe `@Serializable` routes |
| **Backend** | Firebase Auth + Cloud Firestore + Cloud Storage | Core App Ecosystem |
| **AI Architecture** | watsonx Assistant + watsonx Discovery + watsonx.ai | IBM Cloud REST APIs (Ktor) |
| **Location Tracking** | `FusedLocationProviderClient` + Background Foreground Service | Hardware Telemetry |
| **Image Loading** | Coil (`AsyncImage`) | Avatar & Post Media |
| **Typography** | Montserrat (headings) + Nunito (body) | Font System |
| **Targeting** | Min SDK: 28 (Android 9.0) | Target SDK: 37 |

---

## 🧪 For Graders / Testers

### 1. Run the App
Open the project in Android Studio and run ▶ on an emulator or physical device (API 28+). The Firebase configuration file is already bundled within the repository at: `app/google-services.json`. 

*Verified Environment:* Pixel 8 emulator - Android 16 (API 36), arm64-v8a.

### 2. Ready-Made Demo Account
Tap **Sign Up** to create a custom profile, or log in instantly with the grader sandbox credentials:
* **Email:** `grader@roamly.app`
* **Password:** `RoamlyDemo1`

### 3. Core Interactions to Test
* **Home / Track:** Start a trip, allow location permissions, and simulate a GPS route in the emulator (**Extended Controls ⋮ ➔ Location ➔ Play Route**). Tap Stop to watch your telemetry seamlessly auto-publish to the social feed.
* **Discover / Social:** Explore the feed, tap into a post, leave a comment, or follow an author to test multi-user state synchronization.
* **AI Concierge Screen:** Navigate to the AI chat view and input open-ended, semantic search queries (e.g., *"Show me a quiet walking route with a steep incline or viewpoint nearby"*). Observe the RAG engine parsing crowdsourced logs to serve conversational recommendations.

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
