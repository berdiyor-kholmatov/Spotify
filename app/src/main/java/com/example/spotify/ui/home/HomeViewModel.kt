package com.example.spotify.ui.home

import android.content.ContentResolver
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.domain.model.MusicFile
import com.example.spotify.player.PlayerManager
import com.example.spotify.player.PlayerState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val playerState: StateFlow<PlayerState>
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
                    playbackMode = it.playbackMode
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
