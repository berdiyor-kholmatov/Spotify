package com.example.spotify.repository.dataRepository

import com.example.spotify.domain.model.MusicFile
import kotlinx.coroutines.flow.Flow

interface SpotifyDataRepository {

    fun observeMusicFiles(): Flow<List<MusicFile>>

    suspend fun insert(musicFiles: List<MusicFile>)

    suspend fun delete(id: Long)

    suspend fun updateFavorite(id: Long, isFavorite: Boolean)

    suspend fun getFavoriteIds(): List<Long>

}
