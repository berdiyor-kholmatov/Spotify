package com.example.spotify.ui.search

import com.example.spotify.domain.model.MusicFile

data class SearchViewState (
    val musics: List<MusicFile> = listOf(),
    val selectedMusic: MusicFile? = null,
    val filteredMusics: List<MusicFile> = listOf(),
)