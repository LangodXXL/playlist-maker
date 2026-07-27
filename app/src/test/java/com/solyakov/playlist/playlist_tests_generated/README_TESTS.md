# Generated tests for Playlist Maker

Скопируй папку `src/test` из этого архива в модуль `app`.

## 1. Test dependencies

Добавь в `app/build.gradle.kts`:

```kotlin
dependencies {
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("androidx.test:core:1.6.1")
    testImplementation("org.robolectric:robolectric:4.13")
    testImplementation("androidx.room:room-testing:<твоя версия Room>")
}
```

Если зависимости у тебя заведены через `libs.versions.toml`, лучше добавь alias туда и используй их.

## 2. Small production changes needed for deterministic ViewModel tests

### SearchScreenViewModel

Добавь импорт:

```kotlin
import kotlinx.coroutines.CoroutineDispatcher
```

Замени конструктор на:

```kotlin
class SearchScreenViewModel(
    private val tracksRepository: TracksRepository,
    private val historyRepository: SearchHistoryRepository,
    private val trackPlayer: TrackPlayer,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
```

И замени оба места:

```kotlin
viewModelScope.launch(Dispatchers.IO)
```

на:

```kotlin
viewModelScope.launch(ioDispatcher)
```

### PlaylistsViewModel

Добавь импорт:

```kotlin
import kotlinx.coroutines.CoroutineDispatcher
```

Замени конструктор на:

```kotlin
class PlaylistsViewModel(
    private val playlistsRepository: PlaylistsRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
```

И замени:

```kotlin
viewModelScope.launch(Dispatchers.IO)
```

на:

```kotlin
viewModelScope.launch(ioDispatcher)
```

## 3. What is covered

- ViewModel tests:
  - AddPlaylistScreenViewModel
  - FavoriteTracksViewModel
  - SearchScreenViewModel
  - PlaylistsViewModel
  - TrackViewModel
  - TracksInPlaylistViewModel

- Repository tests:
  - SearchHistoryRepositoryImpl with DataStore
  - TracksRepositoryImpl with in-memory Room and fake NetworkClient
  - PlaylistsRepositoryImpl with in-memory Room

- Media3-related tests:
  - Track <-> MediaItem mapper roundtrip
  - invalid MediaItem mapping

`Media3TrackPlayer` itself is not unit-tested directly, because now it creates real `MediaController`, `SessionToken` and depends on `MediaSessionService`. For a pure unit test of that class, first extract a small wrapper/factory over `MediaController`. For internship portfolio, mapper tests + ViewModel tests with fake `TrackPlayer` are already a good practical level.

## 4. Run

```bash
./gradlew testDebugUnitTest
```
