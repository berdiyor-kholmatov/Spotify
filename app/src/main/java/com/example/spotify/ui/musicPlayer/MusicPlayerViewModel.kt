package com.example.spotify.ui.musicPlayer

import androidx.lifecycle.ViewModel
import com.example.spotify.player.PlayerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val playerState: StateFlow<PlayerState>
) : ViewModel() {

    private val _state = MutableStateFlow(MusicPlayerViewState())
    val state = _state.asStateFlow()


    fun onEvent(event: MusicPlayerViewEvents) {

    }






}