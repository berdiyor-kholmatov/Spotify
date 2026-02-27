package com.example.spotify.ui.home

import com.example.spotify.domain.model.MusicFile

sealed class HomeViewEvents {
    data class OnMusicSelected(val music: MusicFile) : HomeViewEvents()
    object OnPlayPauseClicked : HomeViewEvents()
    object OnSkipNextClicked : HomeViewEvents()
    object OnSkipPreviousClicked : HomeViewEvents()
    object OnShuffleRepeatLoopClicked : HomeViewEvents()
    object PermissionGranted : HomeViewEvents()

}