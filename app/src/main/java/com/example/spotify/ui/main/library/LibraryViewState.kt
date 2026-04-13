package com.example.spotify.ui.main.library

import com.example.spotify.domain.model.MusicFile

data class LibraryViewState (
    val musics: List<MusicFile> = listOf(),
)