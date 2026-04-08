package com.example.spotify.ui.search.activeSearch

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import com.example.spotify.service.playerService.PlayerService
import com.example.spotify.ui.home.HomeViewEvents
import com.example.spotify.ui.home.getAlbumArtUri


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveSearchView(state: ActiveSearchViewState, onEvent: (ActiveSearchViewEvents) -> Unit, onBack: () -> Unit) {

    val context = LocalContext.current

    Column() {
        Row() {
            IconButton(
                onClick = { onBack() },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Sharp.ArrowBack,
                    contentDescription = "Back to main search view"
                )
            }
            TextField(
                value = "Search",
                onValueChange = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )
        }
        LazyColumn(modifier = Modifier.systemBarsPadding()) {
            items(state.musics) { music ->

                Row (
                    modifier = Modifier
                        .padding(horizontal = 12.dp,vertical = 8.dp)
                        .clickable {
                            Intent(context, PlayerService::class.java).also {
                                it.action = PlayerService.Actions.START.toString()
                                it.putExtra("URI", music.filePath)
                                ContextCompat.startForegroundService(context, it)
                            }
                        }
                ) {
                    AsyncImage(
                        model = getAlbumArtUri(music.albumId ?: 0),
                        contentDescription = null,
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(MaterialTheme.colorScheme.error)

                        ,
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column ( modifier = Modifier
                        .height(56.dp)
                        .weight(1f)
                        .padding(4.dp)
                        .fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            music.name ?: "null",
                            style = MaterialTheme.typography.bodyLarge,
                            maxLines = 1,
                            modifier = Modifier.offset(y = (-4).dp)
                        )
                        Text(
                            music.artist ?: "null",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }
            }

            items(1){
                Box(modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .height(if ( state.selectedMusic != null ) 130.dp else 60.dp )
                )
            }
        }
    }

}
