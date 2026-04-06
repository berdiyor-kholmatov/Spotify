package com.example.spotify.ui.library

import androidx.compose.ui.graphics.Color
import com.example.spotify.domain.model.MusicFile
import com.example.spotify.ui.home.PlaybackMode

data class LibraryViewState (
    val musics: List<MusicFile> = listOf(),
    val selectedMusic: MusicFile? = null,
    val filteredMusics: List<MusicFile> = listOf(),
)