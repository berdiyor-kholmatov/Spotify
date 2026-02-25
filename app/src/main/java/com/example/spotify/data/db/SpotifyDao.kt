package com.example.spotify.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpotifyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(musicFile: MusicFileEntity)

    @Query("SELECT * FROM musics")
    fun observeMusicFiles(): Flow<List<MusicFileEntity>>
}