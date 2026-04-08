package com.example.spotify.ui.mainView.searchNavigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.spotify.ui.search.activeSearch.ActiveSearchView
import com.example.spotify.ui.search.activeSearch.ActiveSearchViewModel
import com.example.spotify.ui.search.mainSearch.SearchView
import com.example.spotify.ui.search.mainSearch.SearchViewModel

object MainSearch
object ActiveSearch
object ArtistRelatedResults

@Composable
fun SearchNavigation (
    searchNavigationBackStack: MutableList<Any> = remember { mutableStateListOf(MainSearch) }
) {
    NavDisplay(
        backStack = searchNavigationBackStack,
        onBack = { searchNavigationBackStack.removeLastOrNull() },
        entryProvider = { key ->
            when(key) {
                MainSearch -> NavEntry(key) {
                    val searchViewModel: SearchViewModel = hiltViewModel()
                    val state by searchViewModel.state.collectAsState()
                    SearchView(
                        state = state,
                        onEvent = searchViewModel::onEvent,
                    )
                }

                ActiveSearch -> NavEntry(key) {
                    val activeSearchViewModel: ActiveSearchViewModel = hiltViewModel()
                    val state by activeSearchViewModel.state.collectAsState()
                    ActiveSearchView(
                        state = state,
                        onEvent = activeSearchViewModel::onEvent,
                        onBack = { searchNavigationBackStack.removeLastOrNull() }
                    )
                }

                ArtistRelatedResults -> NavEntry(key) {

                }

                else -> NavEntry(Unit) {

                }
            }
        }
    )
}