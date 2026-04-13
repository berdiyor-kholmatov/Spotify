package com.example.spotify.ui.main.artist

import com.example.spotify.domain.model.MusicFile

sealed class ArtistEvents {
    data class OnFavoriteToggle(val music: MusicFile) : ArtistEvents()
}
