package com.example.spotify.ui.main.artist

import com.example.spotify.domain.model.MusicFile

data class ArtistState (
    val musics: List<MusicFile> = listOf(),
    val artistsMusic: List<MusicFile> = listOf(),
    val selectedMusic: MusicFile? = null
)