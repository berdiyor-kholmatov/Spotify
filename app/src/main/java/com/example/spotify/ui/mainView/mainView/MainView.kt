package com.example.spotify.ui.mainView.mainView

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.spotify.ui.mainView.mainNavigation.Home
import com.example.spotify.ui.mainView.mainNavigation.Library
import com.example.spotify.ui.mainView.mainNavigation.MainNavigationDisplay

import com.example.spotify.ui.mainView.mainNavigation.Search
import kotlin.Any

@Composable
fun MainView(
    state: MainViewState,
    onEvent: (MainViewEvents) -> Unit,
    onOpenPlayer: () -> Unit,
    mainNavigationBackStack: MutableList<Any> = remember { mutableStateListOf(Home) }
) {

    val items = listOf(
        BottomNavigationItem (
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            destination = Home
        ),
        BottomNavigationItem (
            title = "Search",
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            destination = Search
        ),
        BottomNavigationItem (
            title = "Library",
            selectedIcon = Icons.Filled.LibraryMusic,
            unselectedIcon = Icons.Outlined.LibraryMusic,
            destination = Library
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Column {
                if (state.selectedMusic != null) {
                    MiniPlayer( state, onClick = {
                        onOpenPlayer()
                    })
                }

                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    // implementation of the spotify like fade
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .align(Alignment.TopCenter)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.5f),
                                        Color.Black.copy(alpha = 1f)
                                    )
                                )
                            )
                    )

                    NavigationBar(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        containerColor = Color.Transparent, // 🔥 ВАЖНО
                        tonalElevation = 0.dp, // убирает тень/overlay
                    ) {
                        val currentDestination = mainNavigationBackStack.lastOrNull()

                        items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = false,
                                onClick = {
                                    if (currentDestination != item.destination) {
                                        mainNavigationBackStack.clear()
                                        mainNavigationBackStack.add(item.destination)
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector =
                                            if (currentDestination == item.destination)
                                                item.selectedIcon
                                            else
                                                item.unselectedIcon,
                                        contentDescription = item.title,
                                        tint = if ( currentDestination == item.destination ) Color.White else Color.Gray,
                                    )
                                },
                                label = {
                                    Text(text = item.title, color = if ( currentDestination == item.destination ) Color.White else  Color.Gray)
                                },
                            )
                        }
                    }
                }
            }
        },

    ) { contentPadding ->
        contentPadding
        MainNavigationDisplay ( mainNavigationBackStack )
    }

}