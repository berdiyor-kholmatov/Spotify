package com.example.spotify.ui.mainView.mainView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.player.PlayerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewViewModel @Inject constructor(
    private val playerState: StateFlow<PlayerState>
) : ViewModel() {

    private val _state = MutableStateFlow(MainViewState())
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

    fun onEvent(event: MainViewEvents) {

    }
}