package com.example.spotify.data.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [MusicFileEntity::class], version = 3, exportSchema = false)
abstract class SpotifyDatabase : RoomDatabase() {
    abstract fun getSpotifyDao(): SpotifyDao
}