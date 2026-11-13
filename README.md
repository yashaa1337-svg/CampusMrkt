# CampusMarket

A clickable UI demo of a student-only marketplace (think OLX/eBay, but every seller is a
verified student and every hand-over happens on campus). Built with Kotlin and Jetpack
Compose. There is **no backend, no sign-in, no payments**: every "real" action shows a
snackbar saying it's a demo.

## What's inside

| Screen | Highlights |
| --- | --- |
| Onboarding | 3 swipeable pages, "Continue with university email" (skips straight to Home) |
| Home | Campus selector, category chips, "Free this week" carousel, 2-column feed |
| Search | Live text search, filter sheet (category, price range, condition, course code, free / swap) |
| Listing detail | Image pager, condition / course chips, seller card with verified badge, hand-drawn meet-up map |
| Sell | Photo slots, category picker, condition segmented buttons, price with Free/Swap toggles, live card preview |
| Chats | Conversation list with Buying/Selling filter and unread badges |
| Chat | Message bubbles, quick replies, composer |
| Profile | Verified badge, stats, tabs for listings / favourites / reviews |

Product photos are gradients + category emoji, so the app is fully offline and has no
image assets. Favourites work in memory for the lifetime of the process.

## Stack

- Kotlin 2.4, Jetpack Compose (BOM 2026.09), Material 3, Navigation Compose
- Android Gradle Plugin 9.5 with built-in Kotlin, Gradle 9.9, version catalog
- minSdk 26, no DI, no network, no database

## Build

```bash
export JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew assembleDebug
./gradlew lint
```

Or open the folder in Android Studio and run the `app` configuration on a Pixel emulator
(API 34+). Every screen and shared component has an `@Preview`.

## Structure

```
app/src/main/java/com/campusmarket/demo/
  MainActivity.kt
  navigation/      AppNavHost (root scaffold + NavHost), BottomBar, Destinations
  data/model/      Listing, User, Chat, Category
  data/mock/       MockData – ~20 listings, 6 users, 5 chats, reviews
  data/            FavoritesState
  ui/theme/        Color, Theme, Type, Shape
  ui/components/   ListingCard, ListingImage, PriceTag, CategoryChip, VerifiedBadge,
                   UserAvatar, SectionHeader, EmptyState, MeetupMap, DemoSnackbar
  ui/screens/      onboarding, home, search, detail, sell, chats, chat, profile
```

Screens take their data as parameters (state hoisting), so previews need no setup.
