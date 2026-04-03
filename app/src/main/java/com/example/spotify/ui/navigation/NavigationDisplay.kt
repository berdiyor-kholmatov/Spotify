package com.example.spotify.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.spotify.ui.home.HomeView
import com.example.spotify.ui.home.HomeViewModel
import com.example.spotify.ui.library.LibraryView
import com.example.spotify.ui.library.LibraryViewModel
import com.example.spotify.ui.musicPlayer.MusicPlayerView
import com.example.spotify.ui.musicPlayer.MusicPlayerViewModel
import com.example.spotify.ui.search.SearchView
import com.example.spotify.ui.search.SearchViewModel


data object Home

data object MusicPlayer

data object Search

data object Library


@Composable
fun NavigationDisplay(
    backStack: MutableList<Any> = remember { mutableStateListOf(Home) }
) {
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is Home -> NavEntry(key) {
                    val homeViewModel: HomeViewModel = hiltViewModel()
                    val state by homeViewModel.state.collectAsState()
                    HomeView(state,  homeViewModel::onEvent)
                }

                is MusicPlayer -> NavEntry(key) {
                    val musicPlayerViewModel: MusicPlayerViewModel = hiltViewModel()
                    val state by musicPlayerViewModel.state.collectAsState()
                    MusicPlayerView(
                        state = state,
                        onEvent = musicPlayerViewModel::onEvent,
                    )
                }

                is Search -> NavEntry(key) {
                    val searchViewModel: SearchViewModel = hiltViewModel()
                    val state by searchViewModel.state.collectAsState()
                    SearchView(
                        state = state,
                        onEvent = searchViewModel::onEvent,
                    )
                }

                is Library -> NavEntry(key) {
                    val libraryViewModel: LibraryViewModel = hiltViewModel()
                    val state by libraryViewModel.state.collectAsState()
                    LibraryView(
                        state = state,
                        onEvent = libraryViewModel::onEvent,
                    )
                }

                else -> NavEntry(Unit) {

                }
            }

        }


    )
}
