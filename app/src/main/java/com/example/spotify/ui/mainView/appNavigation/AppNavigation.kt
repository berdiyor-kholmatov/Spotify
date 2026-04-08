package com.example.spotify.ui.mainView.appNavigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.spotify.ui.mainView.mainView.MainView
import com.example.spotify.ui.mainView.mainView.MainViewViewModel
import com.example.spotify.ui.musicPlayer.MusicPlayerView
import com.example.spotify.ui.musicPlayer.MusicPlayerViewModel

data object MainView
data object PlayerView

@Composable
fun AppNavigationDisplay(
    appNavigationBackStack: MutableList<Any> = remember { mutableStateListOf(MainView) }
){
    NavDisplay(
        backStack = appNavigationBackStack,
        onBack = { appNavigationBackStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                MainView -> NavEntry(key) {
                    val mainViewViewModel: MainViewViewModel = hiltViewModel()
                    val state by mainViewViewModel.state.collectAsState()
                    MainView(
                        state = state,
                        onEvent = mainViewViewModel::onEvent,
                        onOpenPlayer = { appNavigationBackStack.add(PlayerView) }
                    )
                }
                PlayerView -> NavEntry(key) {
                    val musicPlayerViewModel: MusicPlayerViewModel = hiltViewModel()
                    val state by musicPlayerViewModel.state.collectAsState()
                    MusicPlayerView(
                        state = state,
                        onEvent = musicPlayerViewModel::onEvent,
                    )
                }
                else -> NavEntry(Unit) {

                }
            }
        }
    )
}