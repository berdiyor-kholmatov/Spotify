package com.example.spotify.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.provider.MediaStore
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.spotify.R
import com.example.spotify.domain.model.MusicFile
import com.example.spotify.ui.home.HomeViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    private lateinit var player: ExoPlayer
    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
    }

    private val serviceScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )
    private val _state = MutableStateFlow(PlayerState())
    val state = _state.asStateFlow()


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.INIT.toString() -> serviceScope.launch {
                loadFiles()
            }
            Actions.START.toString() -> start(intent.getStringExtra("URI"))
            Actions.STOP.toString() -> stopSelf()
            Actions.PAUSE.toString() -> player.pause()
            Actions.RUN.toString() -> player.play()
            Actions.NEXT.toString() -> next()
            Actions.PREVIOUS.toString() -> previous()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun next() {
        val index = _state.value.musics.indexOfFirst { it.id == _state.value.selectedMusic?.id }
        if (index < _state.value.musics.size - 1) {
            start(_state.value.musics[index + 1].filePath)
            _state.value = _state.value.copy(selectedMusic = _state.value.musics[index + 1])
        }
    }

    private fun previous() {
        val index = _state.value.musics.indexOfFirst { it.id == _state.value.selectedMusic?.id }
        if (index > 0) {
            start(_state.value.musics[index - 1].filePath)
            _state.value = _state.value.copy(selectedMusic = _state.value.musics[index - 1])
        }
    }


    private fun start(filePath: String?) {
        val notification = NotificationCompat.Builder(this, "player_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Run is active")
            .setContentText("Elapsed time: 00:50 \n $filePath")
            .build()
        startForeground(1, notification)
        filePath?.let {
            play(it)
        }
        startForeground(1, notification)
        _state.value = _state.value.copy(isPlaying = true, selectedMusic = _state.value.musics.find { it.filePath == filePath })
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


            val musicFile = mutableListOf<MusicFile>()
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

                    musicFile.add(MusicFile(id, name, duration, filePath))
                }

                _state.value = _state.value.copy(musics = musicFile)
            }
        }
    }

    fun play(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    enum class Actions {
        START,
        STOP,
        PAUSE,
        RUN,
        INIT,
        NEXT,
        PREVIOUS
    }


    override fun onDestroy() {
        serviceScope.cancel()
        player.release()
        super.onDestroy()
    }
}


