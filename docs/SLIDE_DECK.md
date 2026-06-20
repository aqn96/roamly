# Roamly — Final Project Slide Deck Guide

> Build the deck from this file. Each slide lists the **content to put on the slide**, the
> **screenshot** to paste (from `docs/slide_screenshots/`), and the **rubric criteria** it earns.
> Screenshots are from the actual running app.
>
> **Student:** An Nguyen · **Course:** CS5520 (Summer 2026) · **App:** Roamly — *Explore, Contribute, Unlock*

---

## Slide 1 — Title
- **Roamly** — *Explore · Contribute · Unlock*
- A passive travel route-logging app that rewards you for sharing the routes you actually travel — like Strava, but for travel discovery.
- An Nguyen · CS5520 · Summer 2026
- **Screenshot:** none (or `01_login.png` faded as background)

## Slide 2 — Repository & How to Run
- **GitHub:** `https://github.com/aqn96/roamly`
- Firebase config (`google-services.json`) is included in the repo — runs out of the box.
- **Demo account:** `grader@roamly.app` / `RoamlyDemo1` (or sign up your own)
- **Tested on:** Pixel 8 emulator · Android 16 (API 36) · arm64-v8a · min SDK 28
- **Screenshot:** none

## Slide 3 — Concept & Architecture
- **The loop:** 1) Record a trip → 2) Contribute it → 3) Unlock community recommendations
- **Stack:** Jetpack Compose + Material 3, MVVM with unidirectional data flow
- **Flow:** Compose UI → ViewModel (StateFlow) → Repositories → Firebase (Auth · Firestore · Storage)
- **Screenshot:** none — draw the diagram *(source: `docs/PROGRESS.md` "Architecture at a glance")*
- *Rubric: sets up Code Documentation (10) + Cloud Database (60)*

## Slide 4 — Multi-Screen Navigation
- 11 type-safe screens wired through a single Navigation Compose graph
- Screens: Login · Sign Up · Create Profile · Home · Active Trip · Trip Summary · Discover · Post Detail · Profile · Favorites · Location Permission
- Bottom navigation bar (Home / Discover / Favorites) preserves each tab's state
- **Screenshot:** thumbnail row — `05_home.png`, `09_discover.png`, `10_profile.png`
- ✅ **Multi-Screen Application & Navigation (30)**

## Slide 5 — Authentication
- Email/Password sign-up and login via Firebase Authentication
- Input validation + loading states handled in the AuthViewModel
- Session persists — reopening the app skips straight to Home
- **Screenshots:** `01_login.png` + `02_signup.png` (side by side)
- ✅ **User Data Management (20)**

## Slide 6 — Create Profile + Photo Upload (Firebase Storage)
- Pick a profile photo with the Android photo picker
- Image uploads to Firebase Cloud Storage at `profile_photos/{uid}`
- Download URL is saved to the user's Firestore document — photo shows everywhere
- **Screenshots:** `03_photo_picker.png` + `04_create_profile_photo.png`
- ✅ **Cloud Database Integration (60)** · supports **User Data Management (20)**

## Slide 7 — Home
- Personalized home: profile avatar (loaded from Storage), search bar, Start Trip, recommended routes
- **Screenshot:** `05_home.png`

## Slide 8 — GPS Trip Recording (sensor + foreground service)
- Foreground Service + FusedLocationProvider logs the GPS path with an ongoing notification
- Route drawn live on a Compose Canvas with real-time distance, duration, and speed
- "Navigate with Google Maps" hands off navigation via Intent (no Maps API key / billing)
- **Screenshots:** `06_location_permission.png` + `07_active_trip.png`
- ✅ **Application Functionality (20)** — core sensor flow

## Slide 9 — Trip Summary → Auto-Published Post
- On Stop: trip is written to Cloud Firestore and all-time stats update
- The trip is automatically published as a public post — the "Contribute" half of the loop
- **Screenshot:** `08_trip_summary.png` *(crop to the per-trip stats)*
- ✅ **Cloud Database Integration (60)** — writes trips + posts + stats

## Slide 10 — Discover Feed
- Social feed of community routes read from Firestore into a lazy scrollable list
- Filter chips: For You / Nearby / Trending / Following · like & comment counts per card
- **Screenshot:** `09_discover.png`
- ✅ **Cloud Database Integration (60)** · **Multi-User (20)**

## Slide 11 — Post Detail: Like & Comment
- Open a post to see its detail, likes, and comment thread
- Liking and commenting write to Firestore and update counts in real time
- **Screenshot:** `09b_post_detail.png`
- ✅ **Multi-User Features (20)** · **Cloud Database (60)**

## Slide 12 — Multi-User: Other Profiles & Follow
- Tap any traveler to view their profile with real stats and a Follow button
- Follow/unfollow updates both users' follower/following counts in Firestore
- "Travelers to Follow" row suggests new people
- **Screenshot:** `12_other_profile.png` (@maya_c + Follow button)
- ✅ **Multi-User Features (20)**

## Slide 13 — Favorites
- Bookmark any route; favorites stored per-user in Firestore and shown on the Favorites tab
- **Screenshot:** `11_favorites.png`
- ✅ **Cloud Database Integration (60)**

## Slide 14 — Your Profile (stats, edit, log out)
- Own profile: photo, real stats, social counts, computed travel level
- Edit profile, or Log Out — clears the session and returns to Login (switch/create accounts)
- **Screenshot:** `10_profile.png`
- ✅ **User Data Management (20)**

## Slide 15 — Cloud Backend Proof
- Firestore collections: `users`, `posts`, `comments`, `favorites`, `trips`, follow data
- Cloud Storage bucket: `profile_photos/` holding uploaded images
- Real cloud storage **and** retrieval across the whole app
- **Screenshot:** *take these yourself* — Firebase Console → Firestore (a few collections/docs) + Storage (`profile_photos` folder)
- ✅ proves **Cloud Database Integration (60)** end to end

## Slide 16 — Code Quality & Documentation
- What/Who/When header comment on every source file
- `@Preview` on every screen & component
- MVVM + StateFlow · 64 meaningful commits
- **Screenshot:** a code snippet showing a What/Who/When header + a `@Preview` (or the Android Studio preview pane)
- ✅ **Code Documentation and Previews (10)** · supports **GitHub Usage (20)**

## Slide 17 — Closing / Rubric Recap
- Recap: record a trip → contribute it → unlock community recommendations
- 11 screens · real multi-user social features · full Firebase backend (Auth · Firestore · Storage)
- Repo: `https://github.com/aqn96/roamly`
- **Screenshot:** none

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
