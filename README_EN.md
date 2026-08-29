# Spotify — Android Music Player

A local Android music player with a Spotify-inspired interface. The application scans audio files stored on the device, builds a music library, and provides background playback controlled from both the UI and a system notification.

The project covers the complete local audio playback flow: retrieving media through MediaStore, persisting metadata with Room, rendering a reactive Jetpack Compose UI, and controlling ExoPlayer through a foreground service.

## Features

- local audio discovery through Android MediaStore;
- track title, artist, duration, and album artwork display;
- play, pause, and seek controls;
- next and previous track navigation;
- background playback through a foreground service;
- play/pause, next, and previous controls in a media notification;
- compact mini player and dedicated full-screen player;
- track title search with a 500 ms input `debounce`;
- automatic library grouping by artist;
- dedicated track list for each artist;
- adding and removing tracks from favorites;
- dedicated favorites collection;
- favorites persistence with Room;
- dynamic player colors extracted from album artwork;
- navigation between Home, Search, and Library;
- media and notification permission handling across Android versions.

## Architecture

The project follows the MVI pattern with Clean Architecture layer separation.

```text
┌──────────────── Presentation ────────────────┐
│ Jetpack Compose UI                           │
│        │ Events / Intents                    │
│        ▼                                     │
│ ViewModel ──► StateFlow ──► Immutable State  │
└───────────────────┬──────────────────────────┘
                    │
┌──────────────── Domain ──────────────────────┐
│ MusicFile · Repository contract · Mapper API │
│ PlayerManager · Shared player state          │
└───────────────────┬──────────────────────────┘
                    │
┌──────────────── Data ────────────────────────┐
│ Repository implementation · Mapper · Room    │
│ MediaStore loader · DAO · Database           │
└───────────────────┬──────────────────────────┘
                    │
┌──────────────── Playback ────────────────────┐
│ PlayerService · ExoPlayer · MediaSession      │
│ Foreground notification                      │
└──────────────────────────────────────────────┘
```

### MVI

Each screen consists of three main elements:

- **State** — immutable screen state;
- **Event/Intent** — user actions sent to the ViewModel;
- **ViewModel** — processes events and publishes state through `StateFlow`.

The Compose UI observes state and recomposes whenever it changes. This unidirectional data flow separates rendering from event processing and provides a single source of state.

### Clean Architecture

- **Presentation** — Compose screens, UI state, events, and ViewModels;
- **Domain** — the `MusicFile` model, repository contracts, and shared playback state;
- **Data** — Room, DAO, mapper, repository implementation, and local media loading;
- **Service/Playback** — ExoPlayer, background commands, MediaSession, and notification.

Data access is abstracted behind the `SpotifyDataRepository` interface. `DataMapper` converts Room entities into domain models and back. Hilt connects implementations and manages dependencies.

## Data Flow

1. `MusicLoaderService` retrieves device audio through MediaStore.
2. Metadata is converted into domain models and persisted in Room through the repository.
3. The DAO publishes database changes as a `Flow`.
4. `PlayerManager` exposes shared library and playback state through `StateFlow`.
5. ViewModels produce screen-specific state.
6. Compose renders state and sends events to ViewModels or `PlayerService`.
7. `PlayerService` controls ExoPlayer and updates shared state.

## Playback and Local Data

`PlayerService` owns the ExoPlayer instance and runs as a foreground service. `MediaSessionCompat` connects playback with Android and the system media notification. A coroutine updates track progress and distributes it through the shared player state to the mini player and full-screen player.

Room stores track metadata and the `isFavorite` flag. When MediaStore is scanned again, saved favorite IDs are applied to the refreshed library. Changes propagate automatically throughout the application using `Flow` and `StateFlow`.

## Technology Stack

| Area | Technologies |
|---|---|
| Language | Kotlin 2.0.21, Coroutines, Flow |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVI, Clean Architecture, Repository |
| Playback | AndroidX Media3 ExoPlayer |
| Background work | Foreground Service, MediaSessionCompat |
| Persistence | Room, KSP |
| Dependency Injection | Hilt |
| Navigation | Navigation 3 |
| Image loading | Coil |
| Dynamic colors | Android Palette API |
| Build | Gradle Kotlin DSL, Version Catalog |

## Project Structure

```text
app/src/main/java/com/example/spotify/
├── app/                 # Application class
├── data/
│   ├── db/              # Room database, DAO, and entities
│   └── mapper/          # Data ↔ Domain mapping
├── di/                  # Hilt modules
├── domain/              # Domain models and contracts
├── player/              # Shared playback state
├── repository/          # Repository contracts and implementations
├── service/
│   ├── musicsLoaderService/ # MediaStore scanning
│   └── playerService/       # ExoPlayer and background playback
└── ui/
    ├── main/            # Home, Search, Library, and Artist
    ├── musicPlayer/     # Full-screen player
    ├── navigation/      # Application navigation
    └── theme/           # Compose theme
```

## Getting Started

Requirements: Android Studio, JDK 21, Android SDK 36, Android 7.0+ (minSdk 24), and local audio files on the device or emulator.

```bash
git clone https://github.com/berdiyor-kholmatov/Spotify.git
cd Spotify
./gradlew assembleDebug
```

The APK is generated under `app/build/outputs/apk/debug/`. The project can also be launched with the `app` run configuration in Android Studio. On first launch, grant access to audio files; Android 13+ also requires notification permission.

## Build Verification

```bash
./gradlew test
```

The project compiles successfully and passes its configured Gradle test tasks using the JDK bundled with Android Studio.

## Author

[Berdiyor Kholmatov](https://github.com/berdiyor-kholmatov)
