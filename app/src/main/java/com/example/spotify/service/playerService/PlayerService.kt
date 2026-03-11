package com.example.spotify.service.playerService

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.spotify.R
import com.example.spotify.domain.model.MusicFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerService: Service() {


    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private val binder by lazy { PlayerBinder() }

    override fun onBind(intent: Intent?): IBinder = binder

    inner class PlayerBinder: Binder() {
        fun getPlayerService(): PlayerService = this@PlayerService
    }

    private val serviceScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    private lateinit var player: ExoPlayer
    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.INIT.toString() -> serviceScope.launch { loadFiles() }
            Actions.START.toString() -> start(intent.getStringExtra("URI"))
            Actions.STOP.toString() -> stopSelf()
            Actions.PAUSE.toString() -> playPause()
            Actions.RUN.toString() -> playPause()
            Actions.NEXT.toString() -> next()
            Actions.PREVIOUS.toString() -> previous()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun playPause(){
        _playerState.value = _playerState.value.copy(isPlaying = !_playerState.value.isPlaying)
        if (_playerState.value.isPlaying) player.play() else player.pause()
    }
    private fun next() {
        val index = _playerState.value.musics.indexOfFirst { it.id == _playerState.value.selectedMusic?.id }
        if (index < _playerState.value.musics.size - 1) {
            start(_playerState.value.musics[index + 1].filePath)
        }
    }

    private fun previous() {
        val index = _playerState.value.musics.indexOfFirst { it.id == _playerState.value.selectedMusic?.id }
        if (index > 0) {
            start(_playerState.value.musics[index - 1].filePath)
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
        _playerState.value = _playerState.value.copy(isPlaying = true, selectedMusic = _playerState.value.musics.find { it.filePath == filePath })
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
                Log.d("TTT", "musicFiles: $musicFiles")

                _playerState.value = _playerState.value.copy(musics = musicFiles)
            }
        }
    }

    fun play(url: String) { //yep, i have to change receiving url to uri, and actually i have to change it to the Music file as it can be confusing having any uri instead of music uri
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


