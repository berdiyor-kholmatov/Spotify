package com.example.spotify.ui.main.search

import com.example.spotify.domain.model.MusicFile

data class SearchViewState (
    val musics: List<MusicFile> = listOf(),
)