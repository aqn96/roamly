# Roamly — Build Progress Log

> Per-stage record of the Goal 7 implementation, written to double as source material for the
> final presentation slides + video demo. Each stage lists **what was built**, the **rubric
> criteria it satisfies**, and **how to demo it**.

**App:** Roamly — *Explore, Contribute, Unlock* · **Student:** An Nguyen · **Course:** CS5520 (Summer 2026)
**Firebase project:** `roamly-e7c4d` (Auth: Email/Password · Cloud Firestore)

---

## Architecture at a glance

```
UI (Jetpack Compose, Material 3)         ← screens observe state, emit events
   │  collectAsStateWithLifecycle / callbacks
ViewModels (StateFlow + Channel events)  ← single source of truth, survive rotation
   │
Repositories  ──────────────►  Firebase (Auth + Cloud Firestore)
Location: TripSession ◄── TripLocationService (Foreground Service, FusedLocationProvider)
Navigation: type-safe @Serializable routes + single RoamlyNavGraph NavHost
```
Patterns follow course Topic 06/07: ViewModel + StateFlow, one-shot `Channel` events,
`collectAsStateWithLifecycle`, `LazyColumn` with keys, type-safe Navigation Compose.

---

## Stage 1 — Multi-screen navigation ✅

**What was built**
- Type-safe navigation: every screen is a `@Serializable` destination (`RoamlyDestinations.kt`).
- A single `RoamlyNavGraph` `NavHost` wires all 11 screens: auth flow → main tabs → trip flow → detail screens.
- Bottom navigation bar (Home / Discover / Favorites) switches tabs with state preservation.
- `MainActivity` now hosts the graph instead of a single hard-coded screen.

**Rubric criteria addressed**
- **Multi-Screen Application & Navigation (30 pts)** — all navigation flows functional.

**Demo steps**
1. Launch → Login → tap **Sign Up** → **Create Profile** → **Get Started** lands on Home.
2. Tap the bottom tabs to move between Home / Discover / Favorites.
3. Tap a Discover post → Post Detail → back. Tap an avatar → Profile → back.

---

## Stage 2 — GPS trip recording (sensor + Foreground Service) ✅

**What was built**
- `TripLocationService`: a **Foreground Service** using `FusedLocationProviderClient` that logs the
  device's GPS path and posts an ongoing "Recording your route" notification.
- `TripSession`: process-wide `StateFlow` source of truth shared between the service and the UI.
- `TripViewModel`: starts/stops the service and exposes the live route + derived distance as `StateFlow`.
- `ActiveTripScreen`: live duration timer, distance, average speed, a **Canvas polyline** that draws the
  recorded route in real time, and a **"Navigate with Google Maps"** button (launches Maps via Intent).
- `LocationPermissionScreen`: requests location + (Android 13+) notification permission at runtime.

> Design choice: the route is drawn on a Compose **Canvas** and navigation is handed to the Google Maps
> app via Intent — so the app needs **no Maps API key and no billing**, while still demonstrating the
> GPS logging that is the app's core sensor (proposal "Sensor" + "Foreground Service" slides).

**Rubric criteria addressed**
- **Application Functionality (20 pts)** — core trip-recording flow works end to end.
- Supports the proposal's committed Sensor + Foreground Service features.

**Demo steps**
1. Home → **Start Trip** → grant Location (and Notification) permission.
2. Move the device / play a route on the emulator → watch the **blue polyline** draw and Distance/Duration tick.
3. Pull down the status bar → see the persistent **"Roamly is recording your route"** notification.
4. Tap **Navigate with Google Maps** to hand off navigation; **Stop Trip** → Trip Summary.

---

## Stage 3 — Firebase backend wired ✅

- Added the `google-services` plugin (4.4.4) + Firebase BoM (34.14.1) with **Auth** and **Cloud Firestore**.
- Project builds and packages against the real Firebase project `roamly-e7c4d`.

## Stage 4 — Authentication + profile persistence ✅

**What was built**
- `AuthRepository`: the gateway to Firebase Auth + the Firestore `users` collection (sign-up,
  login, sign-out, save/load profile). Firebase handles are lazy so Previews stay safe.
- `RoamlyUser`: the Firestore profile document model (profile, stats, social counts, derived travel level).
- `AuthViewModel`: validates input and exposes a sealed `AuthUiState` (Idle/Loading/Success/Error)
  via `StateFlow` (course Topic 06 pattern).
- `LoginScreen` / `SignUpScreen` / `CreateProfileScreen` now drive real Firebase auth: inline
  errors, loading button text, and navigation only on success.
- `FirebaseExt.awaitResult()`: coroutine bridge for Firebase `Task<T>`.
- The app auto-skips Login when a user is already signed in (session persistence).

**Rubric criteria addressed**
- **User Data Management (20 pts)** — account creation, login, persistent session, profile stored in the cloud.
- Contributes to **Cloud Database Integration (60 pts)** — the `users` collection (create + read).

**Demo steps**
1. **Sign Up** with name/email/password → account is created in Firebase Auth.
2. **Create Profile** (username, home country, travel style…) → written to Firestore `users/{uid}`.
   Show the document live in the Firebase console.
3. Kill and relaunch the app → it opens straight to Home (session persisted).
4. Sign out, then **Log In** with the same credentials → back in.

_Still in progress for the data layer: Firestore CRUD for trips, posts, comments, favorites, follows (multi-user feed)._
