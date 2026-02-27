package com.example.spotify.ui.home

import com.example.spotify.domain.model.MusicFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class HomeViewState(
    val musics: List<MusicFile> = emptyList<MusicFile>(),
    val selectedMusic: MusicFile? = null,
    val isPlaying: Boolean = false,
    val playbackMode: PlaybackMode = PlaybackMode.LOOP,
)

enum class PlaybackMode {
    SHUFFLE,
    LOOP,
    REPEAT_ONE,
}
