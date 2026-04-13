package com.example.spotify.ui.main.activeSearch

import com.example.spotify.domain.model.MusicFile

data class SearchState (
    val musics: List<MusicFile> = listOf(),
    val searchResults: List<MusicFile> = listOf(),
    val selectedMusic: MusicFile? = null,
    val query: String = ""
)