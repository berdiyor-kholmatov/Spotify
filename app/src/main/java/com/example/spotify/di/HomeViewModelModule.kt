package com.example.spotify.di

import android.content.ContentResolver
import android.content.Context
import com.example.spotify.player.PlayerManager
import com.example.spotify.player.PlayerState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.flow.StateFlow

@Module
@InstallIn(SingletonComponent::class)
object HomeViewModelModule {

    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    @Provides
    @Singleton
    fun providePlayerState(
        playerManager: PlayerManager
    ): StateFlow<PlayerState> {
        return playerManager.playerState
    }
}
