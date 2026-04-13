package com.example.spotify.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.spotify.ui.components.Destinations
import com.example.spotify.ui.main.MainView
import com.example.spotify.ui.main.MainViewModel
import com.example.spotify.ui.musicPlayer.MusicPlayerView
import com.example.spotify.ui.musicPlayer.PlayerViewModel

object MainView
object PlayerView
object Search
object Settings
data class Artist(val artistName: String)

@Composable
fun AppNavigationDisplay(
    appNavigationBackStack: MutableList<Any> = remember { mutableStateListOf(MainView) }
){

    val onNavigate: (Destinations) -> Unit = { screen: Destinations ->
        when(screen) {
            Destinations.Main -> appNavigationBackStack.add(MainView)
            Destinations.Player -> appNavigationBackStack.add(PlayerView)
            Destinations.Search -> appNavigationBackStack.add(Search)
            Destinations.Settings -> appNavigationBackStack.add(Settings)
            is Destinations.Artist -> appNavigationBackStack.add(Artist(screen.artistName))
        }
    }

    NavDisplay(
        backStack = appNavigationBackStack,
        onBack = { appNavigationBackStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                MainView -> NavEntry(key) {
                    val mainViewModel: MainViewModel = hiltViewModel()
                    val state by mainViewModel.state.collectAsState()
                    MainView(
                        state = state,
                        onEvent = mainViewModel::onEvent,
                        onNavigate = onNavigate
                    )
                }
                PlayerView -> NavEntry(key) {
                    val playerViewModel: PlayerViewModel = hiltViewModel()
                    val state by playerViewModel.state.collectAsState()
                    MusicPlayerView(
                        state = state,
                        onEvent = playerViewModel::onEvent,
                    )
                }

                Settings -> NavEntry(key) {

                }

                else -> NavEntry(Unit) {

                }
            }
        }
    )
}