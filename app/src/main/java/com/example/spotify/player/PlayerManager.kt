package com.example.spotify.player

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.media3.exoplayer.ExoPlayer
import com.example.spotify.R
import com.example.spotify.domain.model.MusicFile
import com.example.spotify.repository.dataRepository.SpotifyDataRepository
import dagger.hilt.android.qualifiers.ApplicationContext
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
                state.copy(musics = musics)
            }
        }
    }


    fun playPause(){
        _playerState.value = _playerState.value.copy(isPlaying = !_playerState.value.isPlaying)
    }

    fun start(music: MusicFile) {
        _playerState.value = _playerState.value.copy(isPlaying = true, selectedMusic = music)
    }
}
