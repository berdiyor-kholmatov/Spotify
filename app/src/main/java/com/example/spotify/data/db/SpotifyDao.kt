package com.example.spotify.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpotifyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(musicFiles: List<MusicFileEntity>)

    @Query("SELECT * FROM musics")
    fun observeMusicFiles(): Flow<List<MusicFileEntity>>

    @Query("UPDATE musics SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)

    @Query("SELECT id FROM musics WHERE isFavorite = 1")
    suspend fun getFavoriteIds(): List<Long>

    @Query("DELETE FROM musics WHERE id = :id")
    suspend fun delete(id: Long)
}
