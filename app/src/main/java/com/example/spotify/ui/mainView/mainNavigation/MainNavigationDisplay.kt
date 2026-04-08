package com.example.spotify.ui.mainView.mainNavigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.spotify.ui.mainView.homeNavigation.HomeNavigation
import com.example.spotify.ui.mainView.libraryNavigation.LibraryNavigation
import com.example.spotify.ui.mainView.searchNavigation.SearchNavigation


data object Home

data object Search

data object Library


@Composable
fun MainNavigationDisplay(
    backStack: MutableList<Any> = remember { mutableStateListOf(Home) }
) {
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is Home -> NavEntry(key) {
                    HomeNavigation()
                }

                is Search -> NavEntry(key) {
                    SearchNavigation()
                }

                is Library -> NavEntry(key) {
                    LibraryNavigation()
                }

                else -> NavEntry(Unit) {

                }
            }

        }


    )
}
