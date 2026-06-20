# Roamly Final Video Script

This script is written for the presentation deck first, then the live demo after the last slide.
It keeps the tone natural, but still points out the difference between the original idea and the
finished app so the audience can see how the project evolved.

## Slide 1 - Title
**Original idea:** A passive travel app that rewards you for sharing the routes you actually travel.

**Current implementation:** A real Android app called Roamly, built with Compose, Firebase Auth,
Firestore, and background GPS route recording.

**What to say:** "Hi everyone, I’m An Nguyen, and this is Roamly. The idea started as a travel app
that would reward people for sharing the routes they actually walk or drive, but also I wanted to
implement social aspcet and "gamified" the experience. Think of the app like Strava for travelers
instead of fitness focus. By the end, I turned that idea into a working Android app with login, 
cloud data, live trip recording, and a social feed. The main thing I wanted to prove was that 
the concept could move from a sketch into a real app with a clean structure behind it."

**Suggested time:** 15 to 20 seconds

## Slide 2 - What Is Roamly?
**Original idea:** Record a trip, then unlock travel recommendations from other travelers.

**Current implementation:** The app now does that with Firebase sign-in, Firestore profiles, trip
tracking, a discover feed, favorites, comments, follows, and a trip summary screen.

**What to say:** "Roamly is basically a give-to-get travel app. The original plan was simple: you
record the routes you actually travel, and in return you unlock recommendations from other people.
That core idea stayed the same, but the final version now also has authentication, Firestore-backed
routes and profiles, a social feed, favorites, and a full trip summary flow. So the idea stayed
small, but the implementation became a real end-to-end Android product."

**Suggested time:** 20 to 25 seconds

## Slide 3 - Target Audience
**Original idea:** Frequent travelers, backpackers, and digital nomads.

**Current implementation:** The finished app still fits that audience, but it is also usable by any
traveler who wants a low-effort way to log routes and discover local recommendations.

**What to say:** "At the start, I was mainly thinking about frequent travelers, backpackers, and
digital nomads. That is still the audience I had in mind, but the finished app is broad enough that
anyone who wants to log a route and see what other travelers discovered can use it."

**Suggested time:** 15 to 20 seconds

## Slide 4 - Motivation
**Original idea:** Fill the gap between sponsored travel content and real traveler routes.

**Current implementation:** Roamly now actually demonstrates that idea through a real multi-user
feed and a contribute-to-unlock model.

**What to say:** "I built Roamly because most travel apps show polished or sponsored places, but not
the routes that real travelers actually take. I wanted something that felt a little more like Strava,
but for travel discovery. The finished app keeps that motivation, and now the entire loop is real:
you contribute a route, and that contribution unlocks recommendations for you and for other users."

**Suggested time:** 20 to 25 seconds

## Slide 5 - Authentication Flow
**Original idea:** Login, sign up, then create a profile before entering the app.

**Current implementation:** That flow is real now with Firebase Authentication and Firestore-backed
profile persistence.

**What to say:** "This slide was my first pass at the app flow. I wanted a very simple entry point:
log in if you already have an account, or sign up and create a profile if you are new. In the final
build, that flow is not just a sketch anymore. It is backed by Firebase Authentication and Firestore,
so the account and profile data actually persist. Under the hood, that flow is split cleanly between
Compose screens, an `AuthViewModel`, and an `AuthRepository`, which made the state handling much
easier once I started wiring everything together."

**Suggested time:** 20 to 25 seconds

## Slide 6 - Authentication Wireframes
**Original idea:** A clean onboarding path with separate login, registration, and profile setup
screens.

**Current implementation:** The app uses those same screens, but now they have validation, loading
states, and real navigation logic.

**What to say:** "These wireframes show the original onboarding plan a little more clearly. I wanted
the user to understand very quickly whether they were logging in, making a new account, or setting up
a profile. The final app follows that same structure, but now the screens have real validation,
loading states, and actual Firebase navigation instead of just mockups. This is also where the UI
starts to show the architecture a bit, because each screen owns its own inputs while the ViewModel
handles the state changes and the repository talks to Firebase."

**Suggested time:** 20 to 25 seconds

## Slide 7 - Social Profile
**Original idea:** Show stats, trips, and the people you follow.

**Current implementation:** The profile screen now shows travel level, trip counts, distance,
followers, following, and suggested travelers to follow.

**What to say:** "The original profile idea was straightforward: show the traveler’s stats, their past
routes, and the people they follow. The final version goes a bit further and gives each user a real
profile page with trip counts, distance traveled, travel level, follower counts, and suggested
travelers to follow. This is also where the social side starts to feel real, because a profile is no
longer just a static page — it is a live Firestore document that changes as the user keeps traveling."

**Suggested time:** 20 seconds

## Slide 8 - Trip Flow: Contribute
**Original idea:** Tap Start Trip, record the route, stop the trip, and unlock recommendations.

**Current implementation:** Roamly uses a foreground service and FusedLocationProviderClient to log
the path in the background, then saves the trip and unlocks content in Firestore.

**What to say:** "This is the main idea that drives the whole app. The original flow was simple:
start a trip, record where you go, then stop the trip and unlock recommendations. In the finished app,
that actually works. A foreground service keeps logging the GPS route in the background, the live
route draws on screen, and when I stop the trip the route and trip summary are saved to Firestore.
The main technical challenge here is that Android is strict about background work. A normal
background service would get killed too easily, so I had to design this around a foreground service,
a user-visible notification, and a shared state flow instead of just polling location in the UI.
That separation also keeps the trip summary reliable, because the service collects points, the shared
session keeps them alive across screens, and the summary screen writes the finished trip to Firestore
exactly once."

**Suggested time:** 25 to 30 seconds

## Slide 9 - Discovery Flow: Unlock
**Original idea:** A recommendation feed that opens up when you contribute routes.

**Current implementation:** The app now has a live Discover feed, post detail, likes, comments,
favorites, and follow actions.

**What to say:** "The discovery side stayed pretty close to the original plan, but the final version
is much more complete. Instead of just showing a placeholder recommendation feed, Roamly now has a
real Discover tab, post detail screens, likes, comments, saving routes, and following other
travelers. So one user’s trip becomes another user’s content. Architecturally, this is where the
Firestore collections start to matter a lot, because the feed is basically reading and combining the
shared data that all the other screens write."

**Suggested time:** 25 to 30 seconds

## Slide 10 - Backend
**Original idea:** Firebase Authentication, Firestore, Storage, and unlock logic.

**Current implementation:** The core plan stayed the same: Firebase Auth and Firestore still power
users, trips, posts, comments, likes, favorites, and follows. The main implementation difference is
that the live route is drawn in a Compose canvas instead of embedding the Google Maps SDK.

**What to say:** "My original backend diagram included Firebase Authentication, Firestore, Storage,
and some unlock logic. The final app still follows that same backend idea: Firebase Auth and
Firestore handle the user data, trip data, and social interactions. The main simplification is just
the map display — I draw the live route on a Compose canvas instead of embedding the Google Maps SDK,
which avoids API key and billing setup while still showing the exact GPS route. The architecture is
still clean: the UI talks to ViewModels, the ViewModels talk to repositories, and the repositories
are the only layer that knows about Firebase."

**Suggested time:** 25 to 30 seconds

## Slide 11 - Sensor
**Original idea:** Use the phone’s location sensor to track movement.

**Current implementation:** Roamly still follows the same sensor/location idea, using
FusedLocationProviderClient with a foreground service to keep recording route points while the app is
in the background.

**What to say:** "This slide was always about the technical core of the app. I used
FusedLocationProviderClient with a foreground service so Roamly can keep recording route points while
the user is doing something else. So the idea didn't change much from the proposal — it’s still the
phone tracking movement in the background — but the implementation needed the foreground service so
Android would keep it alive. That part taught me the most about permissions, background execution,
and keeping the UI and service in sync. It also forced me to separate responsibilities carefully: the
service collects points, a shared session object stores them, and the Compose UI just observes that
state."

**Suggested time:** 20 to 25 seconds

## Slide 12 - Timeline
**Original idea:** Build the app in stages, from setup to final polish.

**Current implementation:** That rough timeline mostly held up, but the hardest parts turned out to be
the Firebase integration and the background location flow.

**What to say:** "This was my original timeline, and honestly it is pretty close to how the project
played out. I started with setup and authentication, then built the trip flow, then the social
features, and finally the polish and testing. The biggest lesson here was that the backend and the
foreground service took more time than I expected, but they were also the most important pieces to get
right. Once the app had that architecture in place, the rest of the screens started to fall into
place much faster."

**Suggested time:** 20 seconds

## Slide 13 - Thank You
**Original idea:** A simple closing slide with contact info.

**Current implementation:** Use this as the handoff into the live demo.

**What to say:** "That is the original idea and the finished version of Roamly. Next I’ll switch from
the slides to the live app demo so you can see the actual screens, the trip recording, and the social
flow working in real time. The slides are the plan, but the demo is where you can see the whole
architecture doing its job."

**Suggested time:** 10 to 15 seconds

## Slide 14 - Features Not Fully Built / Future Plans
**Suggested slide to add for the rubric**

**Original idea:** Use Firebase Storage and an embedded Google Maps screen for richer visuals.

**Current implementation:** I kept the app on the free tier, so I used a Compose canvas for the live
route and public image URLs instead of a storage upload flow.

**What to say:** "There are a couple of things I would still improve if I had more time. The biggest
one is a real Google Maps view inside the app, plus richer photo upload support. I intentionally kept
the current version on the free tier, which is why the live route is drawn on a canvas and the photos
come from URLs instead of a storage upload flow. If I kept going, I would add a true map base layer,
real image uploads, more advanced recommendation ranking, and a more gamified experience with streaks,
badges, or challenges so the app feels more like a social travel game. So the future plan is less
about rewriting the app and more about upgrading the parts that are intentionally simplified right now."

**Suggested time:** 20 to 25 seconds

## Slide 15 - What I Learned / Reflections
**Suggested slide to add for the rubric**

**Original idea:** A travel app concept with a social unlock mechanic.

**Current implementation:** A full Android app with authentication, background GPS, cloud data, and a
social feed.

**What to say:** "What I learned from this project is that the hard part of app development is not
just making screens look good. It is getting state, permissions, background work, and cloud data to
cooperate. I also got much more comfortable with Compose, Firebase, and debugging on a real device.
Roamly ended up being better than the original sketch because it forced me to build something that
actually works, not just something that looked good in a mockup."

**Suggested time:** 20 to 25 seconds

## Transition to the demo
After the last slide, switch to the actual app and use the demo script in `docs/DEMO_SCRIPT.md`.
That demo should show:
1. Sign up and create a profile
2. Start a trip and record GPS movement
3. Open the discover feed
4. Like, comment, save, and follow
5. Finish on a quick reflection or closing shot
