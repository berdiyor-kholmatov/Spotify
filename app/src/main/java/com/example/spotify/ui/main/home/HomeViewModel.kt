package com.example.spotify.ui.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.player.PlayerState
import com.example.spotify.repository.dataRepository.SpotifyDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val playerState: StateFlow<PlayerState>,
    private val repository: SpotifyDataRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeViewState())
    val state = _state.asStateFlow()

    init { //the order matters, as before where i placed the _state below init it doesn't work
        viewModelScope.launch {
            playerState.collect {
                _state.value = _state.value.copy(
                    isPlaying = it.isPlaying,
                    selectedMusic = it.selectedMusic,
                    musics = it.musics,
                    playbackMode = it.playbackMode,
                    dominantColor = it.dominantColor
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }


    fun onEvent(event: HomeViewEvents) {
        when(event) {
            is HomeViewEvents.OnMusicSelected -> {

            }

            is HomeViewEvents.OnFavoriteToggle -> {
                event.music.id?.let { id ->
                    viewModelScope.launch {
                        repository.updateFavorite(id, !event.music.isFavorite)
                    }
                }
            }

            HomeViewEvents.OnPlayPauseClicked -> {
            }

            HomeViewEvents.OnSkipNextClicked -> {
            }

            HomeViewEvents.OnSkipPreviousClicked -> {
            }

            HomeViewEvents.OnShuffleRepeatLoopClicked -> {
            }
            HomeViewEvents.PermissionGranted -> {
            }
        }
    }
}
