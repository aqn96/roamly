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

## Timed shot list (sums to 180 s)

| # | Scene | Window | Dur | On-screen actions | Voiceover |
|---|-------|--------|-----|-------------------|-----------|
| 1 | **Login** | 0:00–0:10 | 10s | Show Login; briefly tap **Sign Up** to reveal that page, tap back; tap **Log In** → Home | "Roamly uses Firebase email/password auth. Here's the login and sign-up screens." |
| 2 | **Home** | 0:10–0:30 | 20s | Search bar, Recommended Routes row, the central **Start Trip**, bottom nav; tap the avatar | "The Home hub: search, recommended routes, and one-tap trip recording." |
| 3 | **Profile** | 0:30–0:50 | 20s | Stats (trips/distance/unlocked), travel level, followers/following, **Travelers to Follow** — follow one | "My profile: real stats and travel level from my trips, plus travelers to follow." |
| 4 | **Discover** | 0:50–1:10 | 20s | Scroll feed, tap a filter chip, type in search, **like** + **save** a post | "Discover is the multi-user feed — search, filter, like, and save, all from Firestore." |
| 5 | **Post Detail** | 1:10–1:32 | 22s | Open a post; **Follow** author; **like**; type + send a **comment** | "I can follow the author, like the route, and comment — real multi-user interaction." |
| 6 | **Favorites** | 1:32–1:44 | 12s | Saved post appears; open it / remove one | "Saved routes live on the Favorites tab." |
| 7 | **Start Trip + Permission** | 1:44–1:52 | 8s | Tap Start Trip → permission rationale → Allow | "Starting a trip asks for location once, with a clear rationale." |
| 8 | **Active Trip** ⭐ | 1:52–2:26 | 34s | Live **polyline draws** as the Golden Gate Park walk plays; timer/distance/GPS-points tick; recording banner; pull notification shade; Stop Trip | "A Foreground Service logs my GPS path — the route draws live, with a background recording notification." |
| 9 | **Trip Summary** | 2:26–2:42 | 16s | Unlocked badge, all-time stats, past trips | "The trip and its route save to Firestore, stats update, and contributing unlocks recommendations." |
| 10 | **Discover (new post)** | 2:42–2:54 | 12s | The just-recorded trip appears at the top of the feed | "And the route I just walked is instantly shared to the feed." |
| 11 | **Wrap** | 2:54–3:00 | 6s | Back to Home (optionally cut to Firebase console showing the collections) | "Everything is backed by Cloud Firestore. Thanks for watching." |

**Total: 180 s.**

> Pages shown: Login, Sign Up (peek), Home, Profile, Discover, Post Detail, Favorites, Location
> Permission, Active Trip, Trip Summary. *(Create Profile is covered in the slides, or add a 20s
> intro clip that signs up a brand-new account if you want it on video — tell me.)*

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
