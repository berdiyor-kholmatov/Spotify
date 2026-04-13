package com.example.spotify.ui.main.components

sealed class SearchDestinations {

    object ActiveSearch : SearchDestinations()

    data class Artist(val artistName: String) : SearchDestinations()
}