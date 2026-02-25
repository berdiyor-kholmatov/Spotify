package com.example.spotify.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "musics")
data class MusicFileEntity (
    @PrimaryKey val id: Long? = null,
    val name: String? = null,
    val duration: Long? = null,
    val filePath: String? = null,
)