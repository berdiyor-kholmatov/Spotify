package com.example.spotify.ui.main.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.spotify.ui.components.Destinations
import com.example.spotify.ui.main.activeSearch.ActiveSearchViewModel
import com.example.spotify.ui.main.activeSearch.Search
import com.example.spotify.ui.main.artist.Artist
import com.example.spotify.ui.main.artist.ArtistViewModel
import com.example.spotify.ui.main.components.SearchDestinations
import com.example.spotify.ui.main.home.HomeView
import com.example.spotify.ui.main.home.HomeViewModel
import com.example.spotify.ui.main.library.LibraryView
import com.example.spotify.ui.main.library.LibraryViewModel
import com.example.spotify.ui.main.search.SearchView
import com.example.spotify.ui.main.search.SearchViewModel


object Home

object Search

object Library

object ActiveSearch

data class Artist(val artistName: String)

object Settings





@Composable
fun MainNavigation(
    backStack: MutableList<Any>,
    onNavigate: (Destinations) -> Unit
) {

    val onSearchNavigate: (SearchDestinations) -> Unit = { screen: SearchDestinations ->
        when(screen) {
            SearchDestinations.ActiveSearch -> backStack.add(ActiveSearch)
            is SearchDestinations.Artist -> backStack.add(Artist(screen.artistName))
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is Home -> NavEntry(key) {
                    val homeViewModel: HomeViewModel = hiltViewModel()
                    val state by homeViewModel.state.collectAsState()
                    HomeView(
                        state = state,
                        onEvent = homeViewModel::onEvent
                    )
                }

                is Search -> NavEntry(key) {
                    val searchViewModel: SearchViewModel = hiltViewModel()
                    val state by searchViewModel.state.collectAsState()
                    SearchView(
                        state = state,
                        onEvent = searchViewModel::onEvent,
                        onNavigate = onSearchNavigate
                    )
                }

                is Library -> NavEntry(key) {
                    val libraryViewModel: LibraryViewModel = hiltViewModel()
                    val state by libraryViewModel.state.collectAsState()
                    LibraryView(
                        state = state,
                        onEvent = libraryViewModel::onEvent,
                        onNavigate = onSearchNavigate
                    )
                }

                ActiveSearch -> NavEntry(key) {
                    val activeSearchViewModel: ActiveSearchViewModel = hiltViewModel()
                    val state by activeSearchViewModel.state.collectAsState()
                    Search(
                        state = state,
                        onEvent = activeSearchViewModel::onEvent,
                        onBack = { backStack.removeLastOrNull() }
                    )
                }

                is Artist -> NavEntry(key) {
                    Log.d("Artist", key.artistName)
                    val artistViewModel: ArtistViewModel =
                        hiltViewModel<ArtistViewModel, ArtistViewModel.Factory>(
                            creationCallback = { factory ->
                                factory.create(artist = key.artistName)
                            },
                            key = key.artistName.toString()
                        )
                    val state by artistViewModel.state.collectAsState()
                    Artist(
                        artist = key.artistName,
                        state = state,
                        onEvent = artistViewModel::onEvent
                    )
                }

                Settings -> NavEntry(key) {
                    onNavigate(Destinations.Settings)
                }

                else -> NavEntry(Unit) {

                }
            }

        }


    )
}
