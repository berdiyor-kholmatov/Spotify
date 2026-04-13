package com.example.spotify.ui.main.library

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spotify.ui.main.components.SearchDestinations
import com.example.spotify.ui.main.home.getAlbumArtUri
import com.example.spotify.ui.main.search.darken
import com.example.spotify.ui.main.search.extractDominantColor
import com.example.spotify.ui.main.search.getAlbumArtBitmap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryView(state: LibraryViewState, onEvent: (LibraryViewEvents) -> Unit, onNavigate: (SearchDestinations) -> Unit) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Library",
                        fontWeight =  FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = { /*TODO*/ },
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.Search,
                            contentDescription = "Add new library"
                        )
                    }
                    IconButton(
                        onClick = {  },
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Sharp.Add,
                            contentDescription = "Add new library"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Box (modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color.Black)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(11.dp)
                    .background(Color.Black),
                content = {
                    val favorites = state.musics.filter { it.isFavorite }
                    val grouped = state.musics.groupBy { it.artist }

                    items(grouped.size + 1) { index ->

                        Log.d("ddd", "first: $index")


                        if (index == 0) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(115.dp)
                                    .padding(6.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(darken(Color(0xFF1DB954)))
                                    .clickable { onNavigate(SearchDestinations.Artist("Favorites")) }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.TopStart
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Sharp.Favorite,
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            "Favorites",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                            return@items
                        }

                        val artistName = grouped.keys.elementAt(index-1).toString()
                        val bitmap = getAlbumArtBitmap(LocalContext.current, grouped[artistName]?.first()?.albumId)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(115.dp)
                                .padding(6.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(darken(bitmap?.let { extractDominantColor(it)} ?: Color.Gray))
                                .clickable(
                                    onClick = {
                                        onNavigate(SearchDestinations.Artist(grouped.keys.elementAt(index-1).toString()))
                                    }
                                )

                        ){
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.TopStart
                            ){
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .padding(10.dp)
                                ){
                                    Text(
                                        grouped.keys.elementAt(index - 1).toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.BottomEnd
                            ){
                                AsyncImage(
                                    model = getAlbumArtUri(grouped[grouped.keys.elementAt(index - 1).toString()]?.first()?.albumId ?: 0),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(80.dp)
                                        .aspectRatio(1f)
                                        .rotate(15f)
                                        .padding(5.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(MaterialTheme.colorScheme.error)

                                    ,
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}
