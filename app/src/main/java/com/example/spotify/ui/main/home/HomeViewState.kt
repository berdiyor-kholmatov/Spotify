package com.example.spotify.ui.main.home

import androidx.compose.ui.graphics.Color
import com.example.spotify.domain.model.MusicFile

data class HomeViewState(
    val musics: List<MusicFile> = emptyList<MusicFile>(),
    val selectedMusic: MusicFile? = null,
    val isPlaying: Boolean = false,
    val playbackMode: PlaybackMode = PlaybackMode.LOOP,
    val dominantColor: Color = Color.Gray
)

enum class PlaybackMode {
    SHUFFLE,
    LOOP,
    REPEAT_ONE,
}


