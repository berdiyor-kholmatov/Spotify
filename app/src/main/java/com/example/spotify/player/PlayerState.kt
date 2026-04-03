package com.example.spotify.player

import androidx.compose.ui.graphics.Color
import com.example.spotify.domain.model.MusicFile
import com.example.spotify.ui.home.PlaybackMode

data class PlayerState(
    val musics: List<MusicFile> = emptyList(),
    val isPlaying: Boolean = false,
    val selectedMusic: MusicFile? = null,
    val playbackMode: PlaybackMode = PlaybackMode.LOOP,
    val dominantColor: Color = Color.Gray
)