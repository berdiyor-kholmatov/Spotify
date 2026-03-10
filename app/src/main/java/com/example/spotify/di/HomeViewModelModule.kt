package com.example.spotify.di

import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.viewModelScope
import com.example.spotify.repository.homeViewModelRepository.viewModelAndPlayerServiceBinderRepository
import com.example.spotify.service.PlayerService
import com.example.spotify.service.PlayerState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
        playerServiceRepositoryBinder: viewModelAndPlayerServiceBinderRepository
    ): StateFlow<PlayerState> {
        return playerServiceRepositoryBinder.playerState
    }
}