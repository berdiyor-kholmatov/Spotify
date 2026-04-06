package com.example.spotify.ui.search

import com.example.spotify.ui.home.HomeViewEvents

sealed class SearchViewEvents {
    data class SearchingFieldChanged(val query: String) : SearchViewEvents()
}