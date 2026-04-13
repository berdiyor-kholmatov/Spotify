package com.example.spotify.service.musicsLoaderService

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import com.example.spotify.domain.model.MusicFile
import com.example.spotify.repository.dataRepository.SpotifyDataRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MusicLoaderService : Service() {

    @Inject
    lateinit var dataRepository: SpotifyDataRepository

    private val serviceScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        start()

        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        loadFiles()
    }

    private fun loadFiles() = serviceScope.launch(Dispatchers.Default) {
        val favoriteIds = dataRepository.getFavoriteIds()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
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
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val filePathColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)


                while (it.moveToNext()) {
                    val id = it.getLong(idColumn)
                    val name = it.getString(nameColumn)
                        ?.replace(Regex("\\(.*?\\)"), "") // убрать (Lyrics)
                        ?.replace(Regex("\\[.*?\\]"), "")
                        ?.trim()

                    val artist = it.getString(artistColumn)
                    val duration = it.getLong(durationColumn)
                    val filePath = it.getString(filePathColumn)
                    val albumId = it.getLong(albumIdColumn)

                    musicFiles.add(
                        MusicFile(
                            id = id,
                            name = name,
                            artist = artist,
                            duration = duration,
                            filePath = filePath,
                            albumId = albumId,
                            isFavorite = favoriteIds.contains(id)
                        )
                    )
                }

                dataRepository.insert(musicFiles)
//                Log.d("MusicLoaderService", "Music files loaded: $musicFiles")
            }
        }
    }

    enum class Actions {
        START,
        STOP
    }

}
