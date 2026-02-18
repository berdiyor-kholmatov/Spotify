package com.example.spotify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.spotify.ui.theme.SpotifyTheme
import com.example.spotify.ui.theme.mainUI.SpotifyMainView
import com.example.spotify.ui.theme.mainUI.SpotifyViewModel

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
