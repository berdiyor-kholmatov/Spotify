package com.example.spotify.repository.homeViewModelRepository

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.example.spotify.service.PlayerService
import com.example.spotify.service.PlayerState
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


@Singleton
class viewModelAndPlayerServiceBinderRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var playerService: PlayerService? = null
    private val _playerState = MutableStateFlow(PlayerState())

    val playerState = _playerState.asStateFlow()

    val connection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            val binder = service as PlayerService.PlayerBinder
            playerService = binder.getPlayerService()
            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                playerService!!.playerState.collect {
                    _playerState.value = it
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            playerService = null
        }
    }

    init {
        val intent = Intent(context, PlayerService::class.java)
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }
}
