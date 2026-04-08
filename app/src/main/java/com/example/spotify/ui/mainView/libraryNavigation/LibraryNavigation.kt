package com.example.spotify.ui.mainView.libraryNavigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.spotify.ui.library.LibraryView
import com.example.spotify.ui.library.LibraryViewModel

object MainLibrary


@Composable
fun LibraryNavigation (
    libraryNavigationBackStack: MutableList<Any> = remember { mutableStateListOf(MainLibrary) }
) {
    NavDisplay(
        backStack = libraryNavigationBackStack,
        onBack = { libraryNavigationBackStack.removeLastOrNull() },
        entryProvider = { key ->
            when(key) {
                is MainLibrary -> NavEntry(key) {
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