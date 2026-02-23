# Relax

A meditation and sleep Android app built with Kotlin and Jetpack Compose. Features guided meditations, sleep stories, breathing exercises, and ambient soundscapes.

## Features

- **Meditate** — Guided sessions filtered by category (Stress, Anxiety, Sleep, Focus, etc.)
- **Sleep Stories** — Narrated stories for falling asleep
- **Breathe** — Timed breathing exercises (box breathing, 4-7-8, etc.)
- **Soundscapes** — Ambient nature sounds with layered mixing
- **Background playback** — Media controls via notification and lock screen

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.0 |
| UI | Jetpack Compose + Material 3 |
| DI | Hilt |
| Media | Media3 / ExoPlayer |
| Async | Coroutines + StateFlow |
| Build | Gradle Kotlin DSL + Version Catalog |

- **Min SDK:** 26 (Android 8.0)
- **Target SDK:** 34 (Android 14)

## Getting Started

**Prerequisites:** Android Studio Hedgehog or later, JDK 17.

```bash
# Clone
git clone git@github.com:Spoonman1091/Relax.git
cd Relax

# Build debug APK
./gradlew assembleDebug

# Install to connected device/emulator
./gradlew installDebug
```

## Project Structure

```
app/src/main/java/com/relax/app/
├── data/
│   ├── model/          # ContentItem.kt — Meditation, SleepStory, Soundscape, etc.
│   └── repository/     # ContentRepository.kt — in-memory content (Room pending)
├── di/                 # AppModule.kt — Hilt bindings
├── service/            # RelaxMediaService — background MediaSessionService
└── ui/
    ├── breathe/        # Breathing exercise screen + ViewModel
    ├── components/     # GradientCard, RelaxBottomBar, SectionHeader (shared Composables)
    ├── home/           # Home screen + ViewModel
    ├── meditate/       # Meditation browse screen + ViewModel
    ├── navigation/     # RelaxNavigation.kt — NavHost + Screen sealed class
    ├── player/         # Full-screen player + ViewModel
    ├── sleep/          # Sleep stories screen + ViewModel
    ├── soundscape/     # Soundscape mixer screen + ViewModel
    └── theme/          # Color, Type, Theme
```

## Architecture

MVVM with unidirectional data flow. Each screen owns a `*UiState` data class exposed via `StateFlow`; the UI layer only calls ViewModel functions — no state mutation in Composables.

Navigation is single-activity with Compose Navigation. The `Player` screen is reached from any content card and receives `contentId`/`contentType` as path parameters.

## Development Status

All content uses free, publicly available audio from the [Internet Archive](https://archive.org):
- **Meditations** — guided tracks from the *Powerful Mindfulness Meditation* collection
- **Sleep Stories** — LibriVox public domain audiobooks matched to each story's theme (Heidi, The Railway Children, Twenty Thousand Leagues Under the Sea, Arabian Nights, Cranford, The Wind in the Willows)
- **Soundscapes** — ambient nature and environment recordings

Room is declared as a dependency but the database layer has not been implemented yet.

## Building for Release

```bash
./gradlew assembleRelease
```

R8 minification is enabled for release builds. Add ProGuard rules to `app/proguard-rules.pro` as needed.
