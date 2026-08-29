# Spotify — Android Music Player

A local Android music player with a Spotify-inspired interface.

The application scans music stored on the device, organizes it by artist, supports favorites and search, and provides background playback with a mini player and a full-screen player.

## Interface / Интерфейс

### Home and playback / Главный экран и воспроизведение

| Local library | Mini player | Full-screen player |
|:---:|:---:|:---:|
| ![Local music library](./docs/screenshots/home-library.jpg) | ![Home screen with mini player](./docs/screenshots/home-mini-player.jpg) | ![Full-screen music player](./docs/screenshots/full-player.jpg) |
| Треки, обложки, исполнители и избранное | Прогресс и быстрые команды воспроизведения | Обложка, данные трека, перемотка и управление |

### Search / Поиск

| Artist overview | Live search | Search actions |
|:---:|:---:|:---:|
| ![Artist categories](./docs/screenshots/search-artists.jpg) | ![Live track search](./docs/screenshots/search-results.jpg) | ![Search result added to favorites](./docs/screenshots/search-favorite.jpg) |
| Карточки исполнителей с обложками и динамическими цветами | Фильтрация медиатеки по названию во время ввода | Воспроизведение и управление избранным из результатов |

### Library and favorites / Медиатека и избранное

| Library | Favorites | Favorite state |
|:---:|:---:|:---:|
| ![Library grouped by artists](./docs/screenshots/library-artists.jpg) | ![Favorite tracks](./docs/screenshots/favorites.jpg) | ![Favorite indicators on Home](./docs/screenshots/home-favorites.jpg) |
| Favorites и группы исполнителей | Сохранённые композиции с синхронизацией через Room | Единое состояние избранного на связанных экранах |

### Scrolling library / Прокрутка медиатеки

![Scrollable local music library](./docs/screenshots/home-scrolled.jpg)

Длинный список локальных треков с постоянным доступом к нижней навигации и состоянию воспроизведения.

## Documentation

- [English](README_EN.md)
- [Русский](README_RU.md)
