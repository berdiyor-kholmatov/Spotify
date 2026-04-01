package com.example.spotify.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SpotifyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val playerChannel = NotificationChannel(
                "player_channel",
                "Running Notifications",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                setSound(null, null) // отключаем звук
                enableVibration(false) // отключаем вибрацию
            }

            val mediaStoreLoaderChannel = NotificationChannel(
                "media_store_loader_channel",
                "MediaStore Loader",
                NotificationManager.IMPORTANCE_LOW
            )

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(playerChannel)
            notificationManager.createNotificationChannel(mediaStoreLoaderChannel)
        }
    }
}