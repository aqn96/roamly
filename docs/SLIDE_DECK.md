# Roamly — Final Project Slide Deck Guide

> Build the deck from this file. Each slide lists: **what to put on the slide**, the
> **screenshot** to paste (from `docs/slide_screenshots/`), the **narration** (what to say in the
> video demo), and the **rubric criteria** it earns. Screenshots are from the actual running app.
>
> **Student:** An Nguyen · **Course:** CS5520 (Summer 2026) · **App:** Roamly — *Explore, Contribute, Unlock*

---

## Slide 1 — Title
- **On slide:** "Roamly" logo/wordmark · tagline *Explore · Contribute · Unlock* · An Nguyen · CS5520 Summer 2026.
- **Screenshot:** none (or `01_login.png` faded as background).
- **Say:** "This is Roamly — a passive travel route-logging app that rewards you for sharing the routes you actually travel. Contribute your own routes to unlock community recommendations — like Strava, but for travel discovery."

## Slide 2 — Repository & How to Run *(submission requirement)*
- **On slide:**
  - GitHub: `https://github.com/aqn96/roamly`
  - "Firebase config (`google-services.json`) is included in the repo for grading."
  - **Demo account:** `grader@roamly.app` / `RoamlyDemo1`
  - **Tested on:** Pixel 8 emulator · Android 16 (API 36) · arm64-v8a · min SDK 28.
- **Screenshot:** none.
- **Say:** "The full source is on GitHub with the Firebase config included, so it runs out of the box. There's a ready demo account, or you can sign up your own. It was built and verified on a Pixel 8 emulator running Android 16."

## Slide 3 — Concept & Architecture
- **On slide:** the 3-step loop (1. Record a trip → 2. Contribute it → 3. Unlock recommendations) + a simple architecture diagram:
  `Compose UI → ViewModel (StateFlow) → Repositories → Firebase (Auth · Firestore · Storage)`.
- **Screenshot:** none (draw the diagram). *Source: see `docs/PROGRESS.md` "Architecture at a glance".*
- **Say:** "Roamly is built with Jetpack Compose and Material 3, following an MVVM unidirectional-data-flow pattern. Screens observe ViewModels that expose StateFlow; ViewModels talk to repositories; repositories are the single gateway to Firebase Authentication, Cloud Firestore, and Cloud Storage."
- **Rubric:** sets up *Code Documentation (10)* and *Cloud Database (60)*.

## Slide 4 — Multi-Screen Navigation
- **On slide:** "11 type-safe screens, one NavHost" — list the screens (Login, Sign Up, Create Profile, Home, Active Trip, Trip Summary, Discover, Post Detail, Profile, Favorites, Location Permission). Mention bottom nav (Home / Discover / Favorites).
- **Screenshot:** a row of small thumbnails — e.g. `05_home.png`, `09_discover.png`, `10_profile.png`.
- **Say:** "The app has eleven screens wired through a single type-safe Navigation Compose graph, with a bottom navigation bar switching between Home, Discover, and Favorites while preserving each tab's state."
- **Rubric:** ✅ **Multi-Screen Application & Navigation (30)**

## Slide 5 — Authentication
- **On slide:** "Email/Password auth · cloud profile · persistent session."
- **Screenshots:** `01_login.png` + `02_signup.png` (side by side).
- **Say:** "Users sign up and log in with Firebase Authentication. Validation and loading states are handled in the AuthViewModel, and the session persists — so reopening the app skips straight to the home screen."
- **Rubric:** ✅ **User Data Management (20)**

## Slide 6 — Create Profile + Photo Upload (Firebase Storage)
- **On slide:** "Profile saved to Firestore · profile photo uploaded to Firebase Storage."
- **Screenshots:** `03_photo_picker.png` + `04_create_profile_photo.png`.
- **Say:** "When creating a profile, the user picks a photo with the Android photo picker. The image uploads to Firebase Cloud Storage under `profile_photos/{uid}`, and the resulting download URL is saved on the user's Firestore document — so their photo shows everywhere in the app."
- **Rubric:** ✅ **Cloud Database Integration (60)** (Storage) · supports **User Data Management (20)**

## Slide 7 — Home
- **On slide:** "Personalized home · profile avatar · recommended routes."
- **Screenshot:** `05_home.png` (note the avatar top-left loads from Storage).
- **Say:** "The home screen greets the user with their profile photo, a search bar, the Start Trip action, and a row of recommended routes."

## Slide 8 — GPS Trip Recording (sensor + foreground service)
- **On slide:** "Foreground Service + FusedLocationProvider · live route drawn on a Compose Canvas."
- **Screenshots:** `06_location_permission.png` + `07_active_trip.png`.
- **Say:** "Tapping Start Trip requests location permission, then launches a foreground service that logs the GPS path and shows an ongoing notification. The route is drawn live on a Compose Canvas with real-time distance, duration, and speed — and a button hands navigation off to Google Maps."
- **Rubric:** ✅ **Application Functionality (20)** (core sensor flow)

## Slide 9 — Trip Summary → auto-published post
- **On slide:** "Trip saved to Firestore · all-time stats · auto-published as a Discover post (Contribute)."
- **Screenshot:** `08_trip_summary.png` *(crop the per-trip stats; optionally hide All-Time total).*
- **Say:** "When the trip ends, it's written to Cloud Firestore, the user's all-time stats update, and the trip is automatically published as a public post — this is the 'Contribute' half of the give-to-get loop."
- **Rubric:** ✅ **Cloud Database Integration (60)** (writes trips + posts + stats)

## Slide 10 — Discover Feed
- **On slide:** "Social feed of community routes · filter chips (For You / Nearby / Trending / Following)."
- **Screenshot:** `09_discover.png`.
- **Say:** "The Discover tab reads posts from Firestore into a lazy, scrollable feed of routes shared by other travelers, with filter chips and like/comment counts on each card."
- **Rubric:** ✅ **Cloud Database Integration (60)** · **Multi-User (20)**

## Slide 11 — Post Detail: Like & Comment
- **On slide:** "Open a post · like · comment — all persisted to Firestore."
- **Screenshot:** `09b_post_detail.png`.
- **Say:** "Opening a post shows its detail with likes and a comment thread. Liking and commenting write to Firestore and update the counts in real time — meaningful interaction between users."
- **Rubric:** ✅ **Multi-User (20)** · **Cloud Database (60)**

## Slide 12 — Multi-User: Other Profiles & Follow
- **On slide:** "View other travelers' profiles · follow / unfollow · suggested travelers."
- **Screenshot:** `12_other_profile.png` (shows @maya_c + Follow button).
- **Say:** "Tapping another traveler opens their profile with real stats and a Follow button. Following updates both users' follower/following counts in Firestore, and a 'Travelers to Follow' row suggests new people."
- **Rubric:** ✅ **Multi-User Features (20)**

## Slide 13 — Favorites
- **On slide:** "Save routes you love · synced per user in Firestore."
- **Screenshot:** `11_favorites.png` *(optional: save a post first so it's populated).*
- **Say:** "Users can bookmark any route; favorites are stored per-user in Firestore and surfaced on the Favorites tab."
- **Rubric:** ✅ **Cloud Database Integration (60)**

## Slide 14 — Your Profile (stats, edit, log out)
- **On slide:** "Own profile · real stats & travel level · Edit profile · Log Out."
- **Screenshot:** `10_profile.png`.
- **Say:** "A user's own profile shows their photo, real stats, social counts, and a computed travel level. They can edit their profile, or log out — which clears the session and returns to login, so multiple accounts can be tested."
- **Rubric:** ✅ **User Data Management (20)**

## Slide 15 — Cloud Backend Proof *(strongly recommended)*
- **On slide:** screenshots of your **Firebase Console** — Firestore collections (`users`, `posts`, `comments`, `favorites`, `trips`, follow data) **and** the Storage bucket showing `profile_photos/`.
- **Screenshot:** *take these yourself* — Firebase Console → Firestore Database (show a few collections/docs) and Storage (show the `profile_photos` folder).
- **Say:** "Everything is backed by real cloud infrastructure — here are the Firestore collections storing users, posts, comments, likes, follows, and trips, plus the Cloud Storage bucket holding the uploaded profile photos."
- **Rubric:** ✅ proves **Cloud Database Integration (60)** end to end.

## Slide 16 — Code Quality & Documentation *(optional but earns points)*
- **On slide:** bullets — "What/Who/When header on every file · `@Preview` on every screen & component · MVVM + StateFlow · 64 meaningful commits."
- **Screenshot:** a code snippet showing a What/Who/When header + a `@Preview`, or an Android Studio preview pane.
- **Say:** "Every source file is documented with What/Who/When comments and every Composable has a Preview, following the course conventions. The project has a consistent commit history of 64 commits."
- **Rubric:** ✅ **Code Documentation and Previews (10)** · supports **GitHub Usage (20)**

## Slide 17 — Closing / Rubric Recap
- **On slide:** one-line recap of the give-to-get loop + a small table mapping features → rubric criteria (all 8). Thank-you + repo link again.
- **Say:** "Roamly delivers the full loop — record a trip, contribute it, and unlock community recommendations — across eleven screens, real multi-user social features, and a complete Firebase backend with Auth, Firestore, and Storage. Thank you."

---

### Screenshot index (`docs/slide_screenshots/`)
| File | Screen | Slide |
|---|---|---|
| `01_login.png` | Login | 5 |
| `02_signup.png` | Sign Up | 5 |
| `03_photo_picker.png` | Android photo picker | 6 |
| `04_create_profile_photo.png` | Create Profile (photo selected) | 6 |
| `05_home.png` | Home (with avatar) | 7 |
| `06_location_permission.png` | Location permission | 8 |
| `07_active_trip.png` | Active Trip (live route, 0.81 km) | 8 |
| `08_trip_summary.png` | Trip Summary | 9 |
| `09_discover.png` | Discover feed | 10 |
| `09b_post_detail.png` | Post Detail (like/comment) | 11 |
| `10_profile.png` | Own Profile (avatar, Edit, Log Out) | 14 |
| `11_favorites.png` | Favorites | 13 |
| `12_other_profile.png` | Other traveler's profile (Follow) | 12 |

### Still to capture yourself
- **Firebase Console** screenshots for Slide 15 (Firestore collections + Storage bucket).
