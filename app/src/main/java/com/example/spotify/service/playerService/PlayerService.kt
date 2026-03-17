package com.example.spotify.service.playerService

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.spotify.R
import com.example.spotify.domain.model.MusicFile
import com.example.spotify.player.PlayerManager
import com.example.spotify.player.PlayerState
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@AndroidEntryPoint
class PlayerService: Service() {

    @Inject
    lateinit var playerManager: PlayerManager


    private val serviceScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    val playerState: StateFlow<PlayerState>
        get() = playerManager.playerState




    private val binder by lazy { PlayerBinder() }

    override fun onBind(intent: Intent?): IBinder = binder

    inner class PlayerBinder: Binder() {
        fun getPlayerService(): PlayerService = this@PlayerService
    }


    private lateinit var player: ExoPlayer
    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
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
        playerManager.playPause()
        if (playerState.value.isPlaying) player.play() else player.pause()
    }
    private fun next() {
        val index = playerState.value.musics.indexOfFirst { it.id == playerState.value.selectedMusic?.id }
        if (index < playerState.value.musics.size - 1) {
            start(playerState.value.musics[index + 1].filePath)
        }
    }

    private fun previous() {
        val index = playerState.value.musics.indexOfFirst { it.id == playerState.value.selectedMusic?.id }
        if (index > 0) {
            start(playerState.value.musics[index - 1].filePath)
        }
    }

    private fun start(filePath: String?) {
        val index = playerState.value.musics.indexOfFirst { it.filePath == filePath }
        Log.d("AAA", "musics: ${playerState.value.musics}")
        if (index == -1){
            Log.d("AAA", "start: music not found")
            return
        }
        val music = playerState.value.musics[index]

        val notification = NotificationCompat.Builder(this, "player_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Run is active")
            .setContentText("Elapsed time: 00:50")
            .build()
        startForeground(1, notification)

        play(music)

        playerManager.start(music)
    }

    fun play(music: MusicFile) { //yep, i have to change receiving url to uri, and actually i have to change it to the Music file as it can be confusing having any uri instead of music uri
        val uri = music.filePath
        val mediaItem = MediaItem.fromUri(uri ?: "")
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    enum class Actions {
        START,
        STOP,
        PAUSE,
        RUN,
        NEXT,
        PREVIOUS
    }


    override fun onDestroy() {
        serviceScope.cancel()
        player.release()
        super.onDestroy()
    }
}


