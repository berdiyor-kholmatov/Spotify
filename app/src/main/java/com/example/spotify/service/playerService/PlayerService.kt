package com.example.spotify.service.playerService

import android.app.NotificationManager
import android.app.Service
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.palette.graphics.Palette
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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

    private var isForegroundStarted = false

    private lateinit var player: ExoPlayer
    lateinit var mediaSession: MediaSessionCompat

    private lateinit var notificationManager: NotificationManager


    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(this).build()

        notificationManager = getSystemService(NotificationManager::class.java)

        mediaSession = MediaSessionCompat(this, "PlayerService").apply {
            setCallback(object : MediaSessionCompat.Callback() {

                override fun onPlay() {
                    playPause()
                }

                override fun onPause() {
                    playPause()
                }

                override fun onSkipToNext() {
                    next()
                }

                override fun onSkipToPrevious() {
                    previous()
                }
            })

            isActive = true
        }

        mediaSession.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY_PAUSE or
                            PlaybackStateCompat.ACTION_PLAY or
                            PlaybackStateCompat.ACTION_PAUSE or
                            PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                )
                .build()
        )

        observePlayerState()
    }



    private fun observePlayerState() {
        serviceScope.launch {
            playerState.collect { state ->
                updateNotification(state)
            }
        }
    }

    private fun updateNotification(state: PlayerState) {

        val bitmap = getAlbumArtBitmap(this, state.selectedMusic?.albumId)

        val playIntent = MediaButtonReceiver.buildMediaButtonPendingIntent(
            this,
            PlaybackStateCompat.ACTION_PLAY_PAUSE
        )

        val nextIntent = MediaButtonReceiver.buildMediaButtonPendingIntent(
            this,
            PlaybackStateCompat.ACTION_SKIP_TO_NEXT
        )

        val prevIntent = MediaButtonReceiver.buildMediaButtonPendingIntent(
            this,
            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
        )

        val notification = NotificationCompat.Builder(this, "player_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(state.selectedMusic?.name ?: "No track")
            .setContentText(state.selectedMusic?.artist ?: "No artist")
            .setLargeIcon( bitmap ?: BitmapFactory.decodeResource(resources, R.drawable.outline_music_note_24))
            .addAction(R.drawable.outline_skip_previous_24, "Prev", prevIntent)
            .addAction(
                if (state.isPlaying) R.drawable.outline_pause_circle_24 else R.drawable.outline_play_circle_24,
                "Play",
                playIntent
            )
            .addAction(R.drawable.outline_skip_next_24, "Next", nextIntent)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .setOngoing(state.isPlaying)
            .build()

        if (!isForegroundStarted) {
            startForeground(1, notification)
            isForegroundStarted = true
        } else {
            notificationManager.notify(1, notification)
        }
    }




    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        MediaButtonReceiver.handleIntent(mediaSession, intent)


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

        if (index == -1){
            return
        }

        val music = playerState.value.musics[index]

        // Compute color for the track we are about to play, not the previously selected one
        val bitmap = getAlbumArtBitmap(this, music.albumId)

        play(music)

        playerManager.start(music, darken(bitmap?.let { extractDominantColor(it)} ?: Color.Gray))
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

    fun getAlbumArtUri(albumId: Long): Uri {
        return ContentUris.withAppendedId(
            Uri.parse("content://media/external/audio/albumart"),
            albumId
        )
    }

    fun getAlbumArtBitmap(context: Context, albumId: Long?): Bitmap? {
        if (albumId == null) return null

        return try {
            val uri = getAlbumArtUri(albumId)
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }

    fun extractDominantColor(bitmap: Bitmap): Color {
        val palette = Palette.from(bitmap).generate()

        val color = palette.getDominantColor(android.graphics.Color.GRAY)

        return Color(color)
    }

    fun darken(color: Color, factor: Float = 0.7f): Color {
        return Color(
            red = color.red * factor,
            green = color.green * factor,
            blue = color.blue * factor,
            alpha = 1f
        )
    }



    override fun onDestroy() {
        serviceScope.cancel()
        player.release()
        super.onDestroy()
    }
}
