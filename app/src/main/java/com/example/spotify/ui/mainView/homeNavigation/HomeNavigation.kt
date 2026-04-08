package com.example.spotify.ui.mainView.homeNavigation

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


object MainHome


@Composable
fun HomeNavigation (
    homeNavigationBackStack: MutableList<Any> = remember { mutableStateListOf(MainHome) }
) {
    NavDisplay(
        backStack = homeNavigationBackStack,
        onBack = { homeNavigationBackStack.removeLastOrNull() },
        entryProvider = { key ->
            when(key) {
                is MainHome -> NavEntry(key) {
                    val homeViewModel: HomeViewModel = hiltViewModel()
                    val state by homeViewModel.state.collectAsState()
                    HomeView(state,  homeViewModel::onEvent)
                }
                else -> NavEntry(Unit) {
                }
            }
        }
    )
}