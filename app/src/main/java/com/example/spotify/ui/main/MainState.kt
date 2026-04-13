package com.example.spotify.ui.main

import androidx.compose.ui.graphics.Color
import com.example.spotify.domain.model.MusicFile
import com.example.spotify.ui.main.home.PlaybackMode

data class MainState (
    val selectedMusic: MusicFile? = null,
    val isPlaying: Boolean = false,
    val playbackMode: PlaybackMode = PlaybackMode.LOOP,
    val dominantColor: Color = Color.Gray,
    val currentPosition: Long = 0
)