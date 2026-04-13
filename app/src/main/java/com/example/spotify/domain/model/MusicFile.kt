package com.example.spotify.domain.model

data class MusicFile(
    val id: Long? = null,
    val name: String? = null,
    val artist: String? = null,
    val duration: Long? = null,
    val filePath: String? = null,
    val albumId: Long? = null,
    val isFavorite: Boolean = false
)
