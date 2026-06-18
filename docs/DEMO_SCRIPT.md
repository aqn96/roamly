# Roamly — Demo Video Script (timed, 180 s)

> A deterministic, pre-seeded walkthrough that fits the `adb screenrecord` 180-second cap and
> starts at the **Login** screen. Data is seeded *before* recording; the GPS walk is pre-staged.

---

## Pre-seed (done off-camera, before recording)

1. A **demo account** exists in Firebase Auth with a real profile in Firestore:
   - username `an_explorer`, home country, travel style, favorite destination filled in.
2. The account already has **2 recorded trips** → so the **Discover feed** shows 2 posts, the
   **Profile** shows real stats, and **Favorites** has 1 saved post + 1 liked post.
3. The app is left on the **Login** screen with the email/password **pre-typed into the fields**
   (so the first on-camera action is just tapping **Log In** — no risky typing on camera).

## The trip I'll record on camera

- **Route:** *Golden Gate Park Loop — San Francisco* (a walk west→east through the park).
- **Why:** matches the Golden Gate Park imagery already in your proposal deck.
- **Simulated GPS points** (`adb emu geo fix <lon> <lat>`, ~3 s apart):
  ```
  -122.4862 37.7701   (Conservatory of Flowers)
  -122.4830 37.7715
  -122.4795 37.7706
  -122.4760 37.7690   (de Young Museum / Tea Garden)
  -122.4725 37.7686
  -122.4690 37.7695   (Stow Lake)
  -122.4660 37.7702
  ```
- **Photos to gather for slides** (free sources: Unsplash / Wikimedia): Golden Gate Park
  Conservatory of Flowers, Japanese Tea Garden, de Young Museum, Stow Lake, plus a Golden Gate
  Bridge / SF skyline shot for the title slide.

---

## Recorded as TWO clips (each < 180 s; stitch in editing)

### Clip 1 — Account creation + the trip (~95 s)

| # | Scene | Dur | On-screen actions | Voiceover |
|---|-------|-----|-------------------|-----------|
| 1 | **Login → Sign Up** | 8s | Show Login, tap **Sign Up** | "Roamly opens on Login; new travelers tap Sign Up." |
| 2 | **Sign Up** | 18s | Type full name **An Nguyen**, email, password, confirm → **Sign Up** | "Account creation goes through Firebase Authentication." |
| 3 | **Create Profile** | 16s | Type username `an_nguyen`, home country, pick travel style + frequency, favorite destination → **Get Started** | "We capture a profile and save it to Cloud Firestore." |
| 4 | **Home** | 10s | Search bar, Recommended Routes, **Start Trip**, bottom nav | "This is the Home hub." |
| 5 | **Start Trip + Permission** | 7s | Start Trip → permission rationale → Allow | "Starting a trip requests location once." |
| 6 | **Active Trip** ⭐ | 22s | Live **polyline draws** (Golden Gate Park walk); timer / distance / GPS-points tick; recording banner → **Stop Trip** | "A Foreground Service logs my GPS path — the route draws live in the background." |
| 7 | **Trip Summary** | 14s | Unlocked badge, all-time stats, past trips | "The trip and its full route save to Firestore, stats update, and contributing unlocks recommendations." |

### Clip 2 — Multi-user social (~90 s)

| # | Scene | Dur | On-screen actions | Voiceover |
|---|-------|-----|-------------------|-----------|
| 8 | **Discover** | 20s | Feed shows my new post + other travelers'; filter chip; search; **like** + **save** a post | "Discover is the multi-user feed — every traveler's routes, with photos, likes, and saves." |
| 9 | **Post Detail** | 22s | Open a post; **Follow** author; **like**; type + send a **comment** | "I can follow the author, like the route, and comment — all persisted to Firestore." |
| 10 | **Favorites** | 12s | Saved post appears; open / remove | "Saved routes live on the Favorites tab." |
| 11 | **Profile** | 18s | Stats, travel level, followers/following, **Travelers to Follow** (follow one) | "My profile shows real stats and travelers to follow." |
| 12 | **Wrap** | 10s | Back to Home (optionally cut to Firebase console) | "Everything is backed by Cloud Firestore. Thanks for watching." |

> Pages shown across both clips: Login, Sign Up, Create Profile, Home, Location Permission,
> Active Trip, Trip Summary, Discover, Post Detail, Favorites, Profile — **all 11**.

---

## Rubric coverage checklist
- [ ] Navigation across all screens (30) — scenes 1–11
- [ ] Cloud Firestore read+write: users, trips, posts, comments, likes, favorites, follows (60) — 3,4,5,8,9,10
- [ ] User data: login, session, profile (20) — 1,3
- [ ] Multi-user: feed, follow, comment, like (20) — 4,5
- [ ] Polished Material 3 dark UI (20) — all
- [ ] Works end-to-end (20) — all
- [ ] GitHub link + commit history (20) — title/outro slide
- [ ] What/Who/When comments + @Preview (10) — slides
