package com.example.spotify.ui.search.activeSearch

import com.example.spotify.domain.model.MusicFile

data class ActiveSearchViewState (
    val musics: List<MusicFile> = listOf(),
    val selectedMusic: MusicFile? = null,
)