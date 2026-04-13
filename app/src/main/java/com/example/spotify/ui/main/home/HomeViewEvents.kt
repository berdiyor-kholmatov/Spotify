package com.example.spotify.ui.main.home

import android.content.Context
import com.example.spotify.domain.model.MusicFile

sealed class HomeViewEvents {
    data class OnMusicSelected(val music: MusicFile) : HomeViewEvents()
    data class OnFavoriteToggle(val music: MusicFile) : HomeViewEvents()
    object OnPlayPauseClicked : HomeViewEvents()
    object OnSkipNextClicked : HomeViewEvents()
    object OnSkipPreviousClicked : HomeViewEvents()
    object OnShuffleRepeatLoopClicked : HomeViewEvents()
    object PermissionGranted : HomeViewEvents()

}
