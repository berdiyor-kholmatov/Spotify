package com.example.spotify.ui.components

sealed class Destinations {
    object Main : Destinations()
    object Search : Destinations()
    object Player : Destinations()
    object Settings : Destinations()
    data class Artist(val artistName: String) : Destinations()
}