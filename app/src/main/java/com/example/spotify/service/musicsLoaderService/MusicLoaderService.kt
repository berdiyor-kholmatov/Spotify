package com.example.spotify.service.musicsLoaderService

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.spotify.R
import com.example.spotify.domain.model.MusicFile
import com.example.spotify.repository.dataRepository.SpotifyDataRepository
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MusicLoaderService: Service() {

    @Inject
    private lateinit var dataRepository: SpotifyDataRepository

    private val serviceScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when(intent?.action){
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }


        return super.onStartCommand(intent, flags, startId)
    }


    private fun start() {
        val notification = NotificationCompat.Builder(this, "media_store_loader_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Loader is active")
            .setContentText("Elapsed time: 00:50")
            .build()
        startForeground(1, notification)


    }

    private suspend fun loadFiles () = serviceScope.launch(Dispatchers.Default){
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} DESC"

        contentResolver?.let { consRes ->
            val cursor = consRes.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder
            )


            val musicFiles = mutableListOf<MusicFile>()
            cursor?.use {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val filePathColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

                while (it.moveToNext()){
                    val id = it.getLong(idColumn)
                    val name = it.getString(nameColumn)
                    val duration = it.getLong(durationColumn)
                    val filePath = it.getString(filePathColumn)

                    musicFiles.add(MusicFile(id, name, duration, filePath))
                }


                dataRepository.insert(musicFiles)
            }
        }
    }


    enum class Actions{
        START,
        STOP
    }

}