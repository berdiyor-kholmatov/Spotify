package com.example.spotify.ui.search.mainSearch

import com.example.spotify.domain.model.MusicFile

data class SearchViewState (
    val musics: List<MusicFile> = listOf(),
)