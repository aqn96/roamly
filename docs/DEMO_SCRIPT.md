# Roamly - Demo Video Script (timed, 180 s)

> A deterministic, pre-seeded walkthrough that fits the `adb screenrecord` 180-second cap and
> starts at the **Login** screen. Data is seeded *before* recording; the GPS walk is pre-staged.

> **Recorded clips** (in the project root, git-ignored - keep them locally):
> - **`roamly_demo_clip1_account_trip.mp4`** (~94 s) - Login → Sign Up (*An Nguyen*) → Create Profile
>   (*`an_explorer`, United States, Nomad, Frequent, Tokyo Japan*) → Home → Location Permission →
>   Active Trip (live route in the contained map card) → Trip Summary (*1.67 km, "unlocked 2 routes",
>   all-time 1 trip / 2 km / 2 unlocked*).
> - **`roamly_demo_clip2_social.mp4`** (~107 s) - Discover feed → open a post → like → comment → save →
>   Favorites → Profile (stats, travel level, **Travelers to Follow: @maya_c + @bob_c**) → follow a traveler.
>
> **Assembling the final video:** merge Clip 1 then Clip 2 (any editor), add your voiceover using the
> per-scene lines below, and optionally a title slide with the GitHub link. Quick CLI merge:
> ```bash
> printf "file 'roamly_demo_clip1_account_trip.mp4'\nfile 'roamly_demo_clip2_social.mp4'\n" > list.txt
> ffmpeg -f concat -safe 0 -i list.txt -c copy roamly_demo_full.mp4
> ```
>
> **Minor kinks to polish when you re-shoot/edit (optional):** linger a beat on "Travelers to Follow"
> *before* tapping Follow (the followed card disappears on the next load), and the in-app comment is
> demo data. These don't affect functionality.

---

## Pre-seed (done off-camera, before recording)

1. A **demo account** exists in Firebase Auth with a real profile in Firestore:
   - username `an_explorer`, home country, travel style, favorite destination filled in.
2. The account already has **2 recorded trips** → so the **Discover feed** shows 2 posts, the
   **Profile** shows real stats, and **Favorites** has 1 saved post + 1 liked post.
3. The app is left on the **Login** screen with the email/password **pre-typed into the fields**
   (so the first on-camera action is just tapping **Log In** - no risky typing on camera).
   If Firebase still has a signed-in session on the emulator, sign out or clear app data first so the
   app does not auto-open on Home.

## Notes to mention while narrating

- The in-app **route map is a Compose Canvas** rendering the recorded GPS points, used to keep the app
  on Firebase's free tier (no Google Maps billing). **In production this would be a Google Maps SDK
  map with a Polyline overlay** - the recorded data is identical either way.
- The GPS walk is **simulated** on the emulator (`adb emu geo fix` / Extended Controls → Location);
  on a real device the Foreground Service logs actual movement.

## The trip I'll record on camera

- **Route:** *Golden Gate Park Loop - San Francisco* (a walk west→east through the park).
- **Why:** matches the Golden Gate Park imagery already in your proposal deck.
- **Simulated GPS points** - a smooth ~18-point curve (NOT a zig-zag) traced west→east through the
  park (`adb emu geo fix <lon> <lat>`, ~2 s apart). Longitude increases steadily; latitude rises and
  falls in a gentle arc so the polyline looks like a real winding path:
  ```
  -122.4880 37.7700   -122.4846 37.7721   -122.4816 37.7733   -122.4789 37.7737
  -122.4766 37.7736   -122.4743 37.7736   -122.4718 37.7736   -122.4690 37.7733
  -122.4657 37.7721   -122.4640 37.7700
  ```
  *(Generated from `lon = -122.488 + t·0.024`, `lat = 37.770 + 0.0042·sin(πt)` for a natural curve.)*
- **Photos to gather for slides** (free sources: Unsplash / Wikimedia): Golden Gate Park
  Conservatory of Flowers, Japanese Tea Garden, de Young Museum, Stow Lake, plus a Golden Gate
  Bridge / SF skyline shot for the title slide.

---

## Recorded as TWO clips (each < 180 s; stitch in editing)

### Clip 1 - Account creation + the trip (~95 s)

| # | Scene | Dur | On-screen actions | Voiceover |
|---|-------|-----|-------------------|-----------|
| 1 | **Login → Sign Up** | 8s | Show Login, tap **Sign Up** | "Roamly opens on Login; new travelers tap Sign Up." |
| 2 | **Sign Up** | 18s | Type full name **An Nguyen**, email, password, confirm → **Sign Up** | "Account creation goes through Firebase Authentication." |
| 3 | **Create Profile** | 16s | Type username `an_explorer`, home country (United States), pick travel style (Nomad) + frequency (Frequent), favorite destination (Tokyo, Japan) → **Get Started** | "We capture a profile and save it to Cloud Firestore." |
| 4 | **Home** | 10s | Search bar, Recommended Routes, **Start Trip**, bottom nav | "This is the Home hub." |
| 5 | **Start Trip + Permission** | 7s | Start Trip → permission rationale → Allow | "Starting a trip requests location once." |
| 6 | **Active Trip** ⭐ | 22s | Live **polyline draws** (Golden Gate Park walk); timer / distance / GPS-points tick; recording banner → **Stop Trip** | "A Foreground Service logs my GPS path - the route draws live in the background." |
| 7 | **Trip Summary** | 14s | Unlocked badge, all-time stats, past trips | "The trip and its full route save to Firestore, stats update, and contributing unlocks recommendations." |

### Clip 2 - Multi-user social (~90 s)

| # | Scene | Dur | On-screen actions | Voiceover |
|---|-------|-----|-------------------|-----------|
| 8 | **Discover** | 20s | Feed shows my new post + other travelers'; search; feed chips; **like** + **save** a post | "Discover is the multi-user feed - every traveler's routes, with photos, likes, and saves." |
| 9 | **Post Detail** | 22s | Open a post; **Follow** author; **like**; type + send a **comment** | "I can follow the author, like the route, and comment - all persisted to Firestore." |
| 10 | **Favorites** | 12s | Saved post appears; open / remove | "Saved routes live on the Favorites tab." |
| 11 | **Profile** | 18s | Stats, travel level, followers/following, **Travelers to Follow** (follow one) | "My profile shows real stats and travelers to follow." |
| 12 | **Wrap** | 10s | Back to Home (optionally cut to Firebase console) | "Everything is backed by Cloud Firestore. Thanks for watching." |

> Pages shown across both clips: Login, Sign Up, Create Profile, Home, Location Permission,
> Active Trip, Trip Summary, Discover, Post Detail, Favorites, Profile - **all 11**.

---

## 🎙️ Full voiceover narration (read this over the merged video)

> Continuous, first-person script timed to the two clips (~3.5 min). `[on screen]` cues tell you
> where you are; just read the spoken lines. Pause/adjust to match your pacing.

**Intro - title slide / GitHub repo (~12s)**
> "Hi, I'm An Nguyen, and this is **Roamly** - a passive travel app that rewards you for sharing the
> routes you actually travel. Contribute a route, and you unlock recommendations from other
> travelers - think Strava, but for travel. It's built with Kotlin, Jetpack Compose, and Firebase.
> Here's the GitHub repository; let's walk through it."

**[on screen: Login → Sign Up] (~15s)**
> "Roamly opens on a login screen. A new traveler taps Sign Up and creates an account with email and
> password - that's handled by Firebase Authentication. If you've used the app before, your session
> is remembered, so you skip straight to the home screen."

**[on screen: Create Profile] (~12s)**
> "Next, you set up a profile - a username, home country, travel style, how often you travel, and a
> favorite destination. When I tap Get Started, that profile is written to Cloud Firestore."

**[on screen: Home] (~10s)**
> "This is the home hub: a search bar, a row of recommended routes, and one big button to start
> recording a trip, with quick navigation along the bottom."

**[on screen: Location Permission → Allow] (~8s)**
> "Tapping Start Trip asks for location permission once, with a clear explanation of why Roamly needs
> it and how the data is used."

**[on screen: Active Trip - route drawing] (~22s)**
> "Now Roamly is recording. A foreground service logs my GPS path in the background - even while I'd
> normally be navigating in Google Maps - and you can watch the route draw live on the map, with
> distance, duration, and speed updating in real time. A notification shows recording is active. Just
> to note: in production this map would be the Google Maps SDK; here it's drawn on a Compose canvas so
> the app stays on Firebase's free tier - the recorded GPS data is identical either way."

**[on screen: Stop Trip → Trip Summary] (~15s)**
> "When I stop, the trip and its full GPS route are saved to Firestore, my all-time stats update, and
> contributing my route unlocks one to two recommendations - that's the give-to-get model. My past
> trips are read straight back from the cloud."

**[on screen: Discover feed] (~15s)**
> "Over in Discover is the multi-user feed - every traveler's contributed routes, with photos. One
> person's recorded trip becomes everyone else's discovery. I can search the feed, use the chips
> across the top, and Trending currently reorders the list, while I can still like or save any route."

**[on screen: Post Detail - like, comment, save] (~18s)**
> "Opening a post, I can like the route, leave a comment, and bookmark it to save for later. Every one
> of those actions writes to Cloud Firestore and updates instantly - real multi-user interaction."

**[on screen: Favorites] (~8s)**
> "The routes I save show up on my Favorites tab, loaded from my favorites collection in Firestore."

**[on screen: Profile + Travelers to Follow + Follow] (~18s)**
> "My profile shows my real stats and a travel level computed from my trips, along with my follower
> and following counts. And down here are other travelers to follow - I'll follow one, and that action
> updates my following list and removes them from the suggestions below. That following relationship is
> the multi-user heart of Roamly."

**Outro - back to Home / Firebase console (~10s)**
> "Everything you saw - accounts, profiles, trips, posts, comments, likes, favorites, and follows - is
> stored in and retrieved from Cloud Firestore. Thanks for watching!"

---

## Rubric coverage checklist
- [ ] Navigation across all screens (30) - scenes 1–11
- [ ] Cloud Firestore read+write: users, trips, posts, comments, likes, favorites, follows (60) - 3,4,5,8,9,10
- [ ] User data: login, session, profile (20) - 1,3
- [ ] Multi-user: feed, follow, comment, like (20) - 4,5
- [ ] Polished Material 3 dark UI (20) - all
- [ ] Works end-to-end (20) - all
- [ ] GitHub link + commit history (20) - title/outro slide
- [ ] What/Who/When comments + @Preview (10) - slides
