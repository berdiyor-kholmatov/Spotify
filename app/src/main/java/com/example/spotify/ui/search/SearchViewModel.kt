package com.example.spotify.ui.search

import androidx.lifecycle.ViewModel
import com.example.spotify.player.PlayerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val playerState: StateFlow<PlayerState>
) : ViewModel() {
    private val _state = MutableStateFlow(SearchViewState())
    val state = _state.asStateFlow()


    fun onEvent(event: SearchViewEvents) {

    }
}