package com.example.spotify.ui.mainView.searchNavigation

sealed class SearchNavigationDestinations {
    object MainSearch: SearchNavigationDestinations()
    object ActiveSearch: SearchNavigationDestinations()
    data class ArtistRelatedPage ( val artistName: String ): SearchNavigationDestinations()
}
