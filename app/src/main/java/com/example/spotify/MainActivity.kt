package com.example.spotify

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.spotify.service.musicsLoaderService.MusicLoaderService
import com.example.spotify.ui.theme.SpotifyTheme
import com.example.spotify.ui.main.home.HomeViewModel
import com.example.spotify.ui.navigation.AppNavigationDisplay
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val musicViewModel by viewModels<HomeViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, MusicLoaderService::class.java))
        enableEdgeToEdge()
        setContent {
            SpotifyTheme {
//                val playerManagerViewModel: PlayerManagerViewModel = hiltViewModel()
//                val playerState by playerManagerViewModel.state.collectAsState()
//                MainView(playerState)
                AppNavigationDisplay()
            }
        }
    }
}
