package com.example.spotify.ui.main.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify.player.PlayerState
import com.example.spotify.repository.dataRepository.SpotifyDataRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.assisted.AssistedFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory =  ArtistViewModel.Factory::class)
class ArtistViewModel @AssistedInject constructor(
    private val playerState: StateFlow<PlayerState>,
    private val repository: SpotifyDataRepository,
    @Assisted private val artist: String
) : ViewModel() {
    private val _state = MutableStateFlow(ArtistState())
    val state = _state.asStateFlow()


    init { //the order matters, as before where i placed the _state below init it doesn't work
        viewModelScope.launch {
            playerState.collect {
                _state.value = _state.value.copy(
                    musics = it.musics,
                    selectedMusic = it.selectedMusic,
                    artistsMusic = it.musics.filter { music ->
                        if(artist != "Favorites")
                            music.artist == artist
                        else
                            music.isFavorite
                    }
                )
            }
        }
    }
    fun onEvent(event: ArtistEvents) {
        when (event) {
            is ArtistEvents.OnFavoriteToggle -> {
                event.music.id?.let { id ->
                    viewModelScope.launch {
                        repository.updateFavorite(id, !event.music.isFavorite)
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(artist: String): ArtistViewModel
    }
}
