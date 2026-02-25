//package com.example.spotify.di
//
//import android.content.Context
//import androidx.room.Room
//import com.example.spotify.data.db.SpotifyDao
//import com.example.spotify.data.db.SpotifyDatabase
//import dagger.Module
//import dagger.Provides
//
//
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import jakarta.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object DatabaseModule {
//    @Provides
//    @Singleton
//    fun provideSpotifyDatabase(
//        @ApplicationContext context: Context,
//    ) : SpotifyDatabase {
//        return Room.databaseBuilder(context, SpotifyDatabase::class.java, "musics.db")
//            .fallbackToDestructiveMigration(dropAllTables = true)
//            .allowMainThreadQueries()
//            .build()
//    }
//
//    @Provides
//    fun provideSpotifyDao(db: SpotifyDatabase) : SpotifyDao = db.getSpotifyDao()
//}