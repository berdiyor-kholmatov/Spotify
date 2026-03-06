package com.example.spotify.service

import com.example.spotify.domain.model.MusicFile
import com.example.spotify.ui.home.PlaybackMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class PlayerState(
    val isPlaying: Boolean = false,
    val selectedMusic: MusicFile? = null,
    val musics: List<MusicFile> = emptyList(),
    val playbackMode: PlaybackMode = PlaybackMode.LOOP
)
