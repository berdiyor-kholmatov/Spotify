package com.example.spotify.ui.musicPlayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.player.PlayerState
import com.example.spotify.repository.dataRepository.SpotifyDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerState: StateFlow<PlayerState>,
    private val repository: SpotifyDataRepository
) : ViewModel() {

    private val _state = MutableStateFlow(com.example.spotify.ui.musicPlayer.PlayerState())
    val state = _state.asStateFlow()

    init { //the order matters, as before where i placed the _state below init it doesn't work
        viewModelScope.launch {
            playerState.collect {
                _state.value = _state.value.copy(
                    isPlaying = it.isPlaying,
                    selectedMusic = it.selectedMusic,
                    playbackMode = it.playbackMode,
                    dominantColor = it.dominantColor,
                    currentPosition = it.currentPosition
                )
            }
        }
    }

    fun onEvent(event: PlayerEvents) {
        when (event) {
            is PlayerEvents.OnFavoriteToggle -> {
                event.music.id?.let { id ->
                    viewModelScope.launch {
                        repository.updateFavorite(id, !event.music.isFavorite)
                    }
                }
            }
            PlayerEvents.OnShuffleClicked -> {
                // Handle shuffle click
            }

            PlayerEvents.OnRepeatClicked -> {
                // Handle repeat click
            }
        }
    }






}
