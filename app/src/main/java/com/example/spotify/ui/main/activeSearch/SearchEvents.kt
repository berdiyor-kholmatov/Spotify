package com.example.spotify.ui.main.activeSearch

import com.example.spotify.domain.model.MusicFile

sealed class SearchEvents {
    data class Search(val query: String) : SearchEvents()
    data class OnFavoriteToggle(val music: MusicFile) : SearchEvents()
}
