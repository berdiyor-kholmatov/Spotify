package com.example.spotify.di

import android.content.ContentResolver
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeViewModelModule {

    @Provides
    @Singleton
    fun provideContentResolver(@ApplicationContext context: Context) : ContentResolver {
        return context.contentResolver
    }


}