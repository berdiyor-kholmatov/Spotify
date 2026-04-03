package com.example.spotify.ui.library

import androidx.lifecycle.ViewModel
import com.example.spotify.player.PlayerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val playerState: StateFlow<PlayerState>
) : ViewModel() {
    private val _state = MutableStateFlow(LibraryViewState())
    val state = _state.asStateFlow()


fun onEvent(event: LibraryViewEvents) {

}


}