package com.example.spotify.di

import android.content.ContentResolver
import android.content.Context
import com.example.spotify.repository.homeViewModelRepository.ViewModelAndPlayerServiceBinderRepository
import com.example.spotify.service.playerService.PlayerState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
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
    fun providePlayerServiceState(
        playerServiceRepositoryBinder: ViewModelAndPlayerServiceBinderRepository
    ): StateFlow<PlayerState> {
        return playerServiceRepositoryBinder.playerState
    }
}