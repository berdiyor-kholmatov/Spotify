package com.example.spotify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.spotify.ui.theme.SpotifyTheme
import com.example.spotify.ui.home.SpotifyMainView
import com.example.spotify.ui.home.SpotifyViewModel

class MainActivity : ComponentActivity() {

    val musicViewModel by viewModels<SpotifyViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpotifyTheme {
                SpotifyMainView(musicViewModel)
            }
        }
        musicViewModel.setContentResolver(contentResolver)
    }
}
