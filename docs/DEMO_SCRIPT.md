# Roamly — Demo Video Script & Storyboard

> A scene-by-scene plan for the Goal 7 demonstration video. Each scene lists **what to do on the
> screen**, a suggested **voiceover line**, and the **rubric criterion** it proves. Target length
> ~3–4 minutes. Record the emulator screen (Android Studio: **View → Tool Windows → Running Devices →
> the record icon**, or `adb shell screenrecord`).

**Before recording — seed a clean demo state:**
- Sign in with an account that has a real username + at least **2 recorded trips** (so the feed,
  past-trips, and profile stats look populated). Optionally a second account so the feed shows
  another traveler.
- Put the emulator in portrait, full brightness, and clear notifications.

---

### Scene 0 — Title (5s)
- **Show:** the GitHub repo page (`github.com/aqn96/roamly`) or the app icon.
- **Say:** "This is Roamly — a passive travel app that rewards you for sharing the routes you actually
  travel. Contribute a route, unlock recommendations from other travelers. Here's the repo."
- **Proves:** GitHub usage (link required by the rubric).

### Scene 1 — Authentication (25s)
- **Show:** Launch the app → **Login** screen. Tap **Sign Up** → fill Full Name, Email, Password,
  Confirm → **Sign Up** → **Create Profile**: enter username, home country, pick travel style +
  frequency, favorite destination → **Get Started** → lands on **Home**.
- **Say:** "New users sign up with email and password through Firebase Authentication, then build a
  profile that's saved to Cloud Firestore."
- **Proves:** User Data Management (20), Cloud DB write (users), Navigation.

### Scene 2 — Session persistence (10s)
- **Show:** Close the app from recents, reopen it → it goes **straight to Home** (no login).
- **Say:** "The session persists — reopening the app skips login and restores the user."
- **Proves:** User Data Management.

### Scene 3 — Home + navigation (15s)
- **Show:** Home screen — search bar, the central **Start Trip** button, the Recommended Routes row,
  and the bottom navigation. Tap the **avatar** (top-left) → Profile → back.
- **Say:** "The Home screen is the hub: search, recommended routes, and one tap to start recording.
  Bottom navigation moves between Home, Discover, and Favorites."
- **Proves:** Multi-Screen & Navigation (30), UI/UX (20).

### Scene 4 — Record a trip (the core sensor flow) (45s) ⭐
- **Show:** Tap **Start Trip** → **Location Permission** rationale → **Allow** (grant the OS dialog) →
  **Active Trip** screen. Walk a route (on the emulator, use *Extended controls → Location* to play a
  route, or set several points). Watch the **blue polyline draw live**, the **timer**, **distance**,
  and **GPS-points** update, plus the **"recording in background"** banner. Pull down the status bar
  to show the persistent **Foreground Service notification**. Tap **Stop Trip**.
- **Say:** "Tapping Start Trip launches a Foreground Service that logs my GPS path with the Fused
  Location Provider. The route draws in real time on the map, distance and duration update live, and a
  notification shows recording is active in the background — exactly like Strava, but for travel."
- **Proves:** Application Functionality (20), the proposal's Sensor + Foreground Service.

### Scene 5 — Trip Summary + give-to-get unlock (20s)
- **Show:** **Trip Summary** — the latest route, the **"You unlocked N new routes!"** badge,
  **All-Time Stats** (trips / distance / unlocked), and the **Past Trips** list.
- **Say:** "When I stop, the trip and its full GPS route are saved to Firestore, my stats update, and
  contributing unlocks one to two recommendations — the give-to-get model. Past trips are read back
  from the cloud."
- **Proves:** Cloud DB (60), Application Functionality.

### Scene 6 — Discover feed (multi-user) (25s)
- **Show:** Tap **Discover**. Scroll the feed of route posts from travelers. Tap a **filter chip**
  (e.g. Trending), type in **search**. Tap a post's **save (bookmark)** and **like (heart)** — watch
  the counts react.
- **Say:** "Discover is the multi-user feed — every traveler's contributed routes appear here. I can
  search, filter, like, and save posts, all backed by Firestore."
- **Proves:** Multi-User (20), Cloud DB (60).

### Scene 7 — Post Detail: comment + follow (25s)
- **Show:** Tap a post → **Post Detail**. Tap **Follow** on the author. **Like** the post. Type a
  **comment** and send it → it appears in the comments list and the count increments.
- **Say:** "Opening a post, I can follow the author, like the route, and leave a comment — real
  multi-user interaction. Every action writes to Cloud Firestore and updates instantly."
- **Proves:** Multi-User (20), Cloud DB (60).

### Scene 8 — Favorites (15s)
- **Show:** Tap **Favorites** → the post I saved earlier appears. Tap a saved post to open it; tap the
  bookmark to remove one and watch it disappear.
- **Say:** "Saved routes live on the Favorites tab, loaded from my favorites collection in Firestore."
- **Proves:** Cloud DB, Navigation.

### Scene 9 — Profile (20s)
- **Show:** Tap the **avatar** → **Profile**. Show the banner, avatar, **stats** (trips / distance /
  unlocked), **travel level**, **followers/following** counts, and the **Travelers to Follow** row —
  tap **Follow** on a suggested traveler. Tap a suggested traveler to view **their** profile.
- **Say:** "My profile shows my real stats and travel level computed from my trips, my social counts,
  and suggested travelers to follow — and I can view anyone else's profile too."
- **Proves:** Multi-User (20), User Data (20), Cloud DB (60).

### Scene 10 — Wrap (10s)
- **Show:** Back to Home; optionally the Firebase console showing the `users`, `trips`, and `posts`
  collections with live data.
- **Say:** "Everything — auth, profiles, trips, posts, comments, likes, favorites, and follows — is
  stored and retrieved from Cloud Firestore. Thanks for watching."
- **Proves:** Cloud DB (60) summary.

---

## Rubric coverage checklist (tick as you film)
- [ ] Multi-screen navigation across all 11 screens (30)
- [ ] Cloud Firestore read + write: users, trips, posts, comments, likes, favorites, follows (60)
- [ ] User data: sign-up, login, persistent session, profile (20)
- [ ] Multi-user: feed of others, follow, comment, like (20)
- [ ] Polished, consistent Material 3 dark UI (20)
- [ ] App works end-to-end without crashes (20)
- [ ] GitHub repo link + commit history shown or mentioned (20)
- [ ] (Mention in slides) What/Who/When comments + @Preview in code (10)
