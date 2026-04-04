package com.example.spotify

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.spotify.player.PlayerManager
import com.example.spotify.player.PlayerManagerViewModel
import com.example.spotify.player.PlayerState
import com.example.spotify.service.musicsLoaderService.MusicLoaderService
import com.example.spotify.ui.home.HomeView
import com.example.spotify.ui.theme.SpotifyTheme
import com.example.spotify.ui.home.HomeViewModel
import com.example.spotify.ui.mainView.MainView
import com.example.spotify.ui.musicPlayer.MusicPlayerViewModel
import com.example.spotify.util.TAG
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val musicViewModel by viewModels<HomeViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, MusicLoaderService::class.java))
        enableEdgeToEdge()
        setContent {
            SpotifyTheme {
                val playerManagerViewModel: PlayerManagerViewModel = hiltViewModel()
                val playerState by playerManagerViewModel.state.collectAsState()
                MainView(LocalContext.current, playerState)
            }
        }
    }
}
