package com.example.spotify.service.playerService

import com.example.spotify.domain.model.MusicFile
import com.example.spotify.ui.home.PlaybackMode

data class PlayerState(
    val musics: List<MusicFile> = emptyList(),
    val isPlaying: Boolean = false,
    val selectedMusic: MusicFile? = null,
    val playbackMode: PlaybackMode = PlaybackMode.LOOP
)
