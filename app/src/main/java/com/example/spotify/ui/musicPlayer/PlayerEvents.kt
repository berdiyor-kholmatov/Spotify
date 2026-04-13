package com.example.spotify.ui.musicPlayer

import com.example.spotify.domain.model.MusicFile

sealed class PlayerEvents {
    data class OnFavoriteToggle(val music: MusicFile) : PlayerEvents()
    object OnShuffleClicked : PlayerEvents()

    object OnRepeatClicked : PlayerEvents()

}
