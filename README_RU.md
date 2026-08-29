# Spotify — музыкальный плеер для Android

Локальный музыкальный плеер с интерфейсом в стиле Spotify. Приложение сканирует аудиофайлы на Android-устройстве, формирует медиатеку и воспроизводит музыку в фоне с управлением из интерфейса и системного уведомления.

Проект охватывает полный сценарий локального аудиоплеера: получение данных через MediaStore, хранение метаданных в Room, реактивный UI на Jetpack Compose и управление ExoPlayer через foreground service.

## Функциональность

- сканирование локальных аудиофайлов через Android MediaStore;
- отображение названия, исполнителя, длительности и обложки альбома;
- воспроизведение, пауза и перемотка трека;
- переход к следующей и предыдущей композиции;
- фоновое воспроизведение через foreground service;
- управление play/pause, next и previous из media notification;
- мини-плеер и отдельный полноэкранный экран плеера;
- поиск по названию трека с `debounce` 500 мс;
- автоматическая группировка медиатеки по исполнителям;
- отдельный список композиций выбранного исполнителя;
- добавление и удаление треков из избранного;
- отдельная коллекция избранных композиций;
- сохранение избранного в Room;
- динамическая цветовая схема на основе доминирующего цвета обложки;
- навигация между Home, Search и Library;
- обработка разрешений на чтение аудио и показ уведомлений для разных версий Android.

## Архитектура

Проект построен на MVI-паттерне с разделением на слои Clean Architecture.

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

Каждый экран представлен тремя основными элементами:

- **State** — неизменяемое состояние экрана;
- **Event/Intent** — действия пользователя, передаваемые во ViewModel;
- **ViewModel** — обрабатывает события и публикует состояние через `StateFlow`.

Compose UI подписывается на state и перерисовывается при его изменении. Однонаправленный поток данных отделяет отображение от обработки событий и обеспечивает единый источник состояния.

### Clean Architecture

- **Presentation** — Compose-экраны, UI state, события и ViewModel;
- **Domain** — модель `MusicFile`, контракты repository и общее состояние воспроизведения;
- **Data** — Room, DAO, mapper, реализация repository и загрузка локальной медиатеки;
- **Service/Playback** — ExoPlayer, фоновые команды, MediaSession и уведомление.

Доступ к данным скрыт за интерфейсом `SpotifyDataRepository`. `DataMapper` преобразует Room entities в доменные модели и обратно. Hilt связывает реализации и управляет зависимостями.

## Поток данных

1. `MusicLoaderService` получает аудиофайлы устройства через MediaStore.
2. Метаданные преобразуются в доменные модели и сохраняются в Room через repository.
3. DAO публикует изменения как `Flow`.
4. `PlayerManager` предоставляет единое состояние медиатеки и воспроизведения через `StateFlow`.
5. ViewModel формируют state конкретных экранов.
6. Compose UI отображает state и отправляет события во ViewModel или `PlayerService`.
7. `PlayerService` управляет ExoPlayer и обновляет общее состояние.

## Воспроизведение и локальные данные

`PlayerService` владеет экземпляром ExoPlayer и работает как foreground service. `MediaSessionCompat` связывает воспроизведение с Android и системным media notification. Прогресс композиции обновляется корутиной и передаётся через общее состояние плеера в мини-плеер и полноэкранный экран.

Room хранит метаданные композиций и признак `isFavorite`. При повторном сканировании MediaStore сохранённые идентификаторы избранных треков применяются к обновлённой медиатеке. Изменения автоматически распространяются по приложению через `Flow` и `StateFlow`.

## Технологии

| Направление | Технологии |
|---|---|
| Язык | Kotlin 2.0.21, Coroutines, Flow |
| UI | Jetpack Compose, Material 3 |
| Архитектура | MVI, Clean Architecture, Repository |
| Воспроизведение | AndroidX Media3 ExoPlayer |
| Фоновая работа | Foreground Service, MediaSessionCompat |
| Хранение данных | Room, KSP |
| Dependency Injection | Hilt |
| Навигация | Navigation 3 |
| Изображения | Coil |
| Динамические цвета | Android Palette API |
| Сборка | Gradle Kotlin DSL, Version Catalog |

## Структура проекта

```text
app/src/main/java/com/example/spotify/
├── app/                 # Application class
├── data/
│   ├── db/              # Room database, DAO и entities
│   └── mapper/          # Data ↔ Domain преобразования
├── di/                  # Hilt modules
├── domain/              # Доменные модели и контракты
├── player/              # Общее состояние плеера
├── repository/          # Контракты и реализации repository
├── service/
│   ├── musicsLoaderService/ # Сканирование MediaStore
│   └── playerService/       # ExoPlayer и background playback
└── ui/
    ├── main/            # Home, Search, Library, Artist
    ├── musicPlayer/     # Полноэкранный плеер
    ├── navigation/      # Навигация приложения
    └── theme/           # Compose theme
```

## Запуск

Требования: Android Studio, JDK 21, Android SDK 36, Android 7.0+ (minSdk 24) и локальные аудиофайлы на устройстве или эмуляторе.

```bash
git clone https://github.com/berdiyor-kholmatov/Spotify.git
cd Spotify
./gradlew assembleDebug
```

APK создаётся в `app/build/outputs/apk/debug/`. Проект также можно запустить через конфигурацию `app` в Android Studio. При первом запуске предоставьте доступ к аудиофайлам; на Android 13+ также требуется разрешение на уведомления.

## Проверка сборки

```bash
./gradlew test
```

Проект успешно компилируется и проходит настроенные Gradle test tasks на JDK из Android Studio.

## Автор

[Berdiyor Kholmatov](https://github.com/berdiyor-kholmatov)
