package com.example.spotify.ui.main.activeSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.player.PlayerState
import com.example.spotify.repository.dataRepository.SpotifyDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class ActiveSearchViewModel @Inject constructor(
    private val playerState: StateFlow<PlayerState>,
    private val repository: SpotifyDataRepository
) : ViewModel() {
    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()


    init { //the order matters, as before where i placed the _state below init it doesn't work
        viewModelScope.launch {
            playerState.collect {
                val query = _state.value.query
                val filtered = if (query.isBlank()) it.musics else it.musics.filter { music ->
                    music.name?.contains(query, ignoreCase = true) ?: false
                }
                _state.value = _state.value.copy(
                    musics = it.musics,
                    selectedMusic = it.selectedMusic,
                    searchResults = filtered
                )
            }
        }

        viewModelScope.launch {
            _state
                .map { it.query }
                .debounce(500)
                .distinctUntilChanged()
                .mapLatest { query ->
                    if (query.isBlank()) {
                        _state.value.musics
                    } else {
                        _state.value.musics.filter {
                            it.name?.contains(query, ignoreCase = true) ?: false
                        }
                    }
                }
                .collect { results ->
                    _state.update {
                        it.copy(searchResults = results)
                    }
                }
        }

    }
    fun onEvent(event: SearchEvents) {
        when(event) {
            is SearchEvents.Search -> {
                _state.value = _state.value.copy(query = event.query)
            }
            is SearchEvents.OnFavoriteToggle -> {
                event.music.id?.let { id ->
                    viewModelScope.launch {
                        repository.updateFavorite(id, !event.music.isFavorite)
                    }
                }
            }
        }
    }
}
