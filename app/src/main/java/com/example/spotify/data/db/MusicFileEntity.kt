package com.example.spotify.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "musics")
data class MusicFileEntity (
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String? = null,
    val artist: String? = null,
    val duration: Long? = null,
    val filePath: String? = null,
    val albumId: Long? = null,
    val isFavorite: Boolean = false
)
