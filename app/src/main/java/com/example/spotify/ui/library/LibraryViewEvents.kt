package com.example.spotify.ui.library

import com.example.spotify.ui.search.SearchViewEvents

sealed class LibraryViewEvents {
    data class ArtistClicked(val artist: String) : LibraryViewEvents()
}