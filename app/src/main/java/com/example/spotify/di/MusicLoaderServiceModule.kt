package com.example.spotify.di

import com.example.spotify.repository.dataRepository.SpotifyDataRepository
import com.example.spotify.repository.dataRepository.SpotifyDataRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MusicLoaderServiceModule {
    @Provides
    @Singleton
    fun providesMusicLoaderServiceRepository(impl: SpotifyDataRepositoryImpl): SpotifyDataRepository = impl
}
