package com.example.spotify.player

//import android.graphics.Color
import androidx.compose.ui.graphics.Color
import com.example.spotify.domain.model.MusicFile
import com.example.spotify.repository.dataRepository.SpotifyDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlayerManager @Inject constructor(
    private val repository: SpotifyDataRepository,
) {
    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private val serviceScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    init {
        serviceScope.launch {
            observeMusicFiles()
        }
    }

    private suspend fun observeMusicFiles() {
        repository.observeMusicFiles().collect { musics ->
            _playerState.update { state ->
                val updatedSelected = state.selectedMusic?.id?.let { id ->
                    musics.firstOrNull { it.id == id } ?: state.selectedMusic
                }
                state.copy(
                    musics = musics,
                    selectedMusic = updatedSelected
                )
            }
        }
    }


    fun playPause(){
        _playerState.value = _playerState.value.copy(isPlaying = !_playerState.value.isPlaying)
    }

    fun start(music: MusicFile, color: Color) {
        _playerState.value = _playerState.value.copy(isPlaying = true, selectedMusic = music, dominantColor = color)

    }

    fun updatePosition(position: Long) {
        _playerState.update {
            it.copy(
                currentPosition = position
            )
        }
    }
}
