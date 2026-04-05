package com.example.spotify.ui.mainView

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.spotify.player.PlayerState
import com.example.spotify.ui.navigation.Home
import com.example.spotify.ui.navigation.Library
import com.example.spotify.ui.navigation.MusicPlayer
import com.example.spotify.ui.navigation.NavigationDisplay
import com.example.spotify.ui.navigation.Search
import kotlin.Any

@Composable
fun MainView(
    context: Context,
    state: PlayerState,
    backStack: MutableList<Any> = remember { mutableStateListOf(Home) }
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

    val selectedItemIndex = rememberSaveable {
        mutableStateOf(0)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {

            if (backStack.lastOrNull() != MusicPlayer) {
                Column {
                    if (state.selectedMusic != null) {
                        MiniPlayer(context, state, onClick = {
                            backStack.add(MusicPlayer)
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
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    selected = false,
                                    onClick = {
                                        selectedItemIndex.value = index
                                        if (backStack.lastOrNull() != item.destination) {
                                            backStack.clear()
                                            backStack.add(item.destination)
                                        }
                                    },
                                    icon = {
                                        Icon(
                                            imageVector =
                                                if (index == selectedItemIndex.value)
                                                    item.selectedIcon
                                                else
                                                    item.unselectedIcon,
                                            contentDescription = item.title,
                                            tint = if ( index == selectedItemIndex.value ) Color.White else Color.Gray,
                                        )
                                    },
                                    label = {
                                        Text(text = item.title, color = if ( index == selectedItemIndex.value ) Color.White else  Color.Gray)
                                    },
                                )
                            }
                        }
                    }
                }
            }
        },

    ) { contentPadding ->
        NavigationDisplay ( context, backStack )
    }

}