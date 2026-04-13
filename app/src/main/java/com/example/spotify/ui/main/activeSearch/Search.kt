package com.example.spotify.ui.main.activeSearch

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.spotify.service.playerService.PlayerService
import com.example.spotify.ui.main.home.getAlbumArtUri


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(state: SearchState, onEvent: (SearchEvents) -> Unit, onBack: () -> Unit) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .systemBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .drawWithContent {
                    drawContent()

                    val strokeWidth = 1.dp.toPx()

                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )
                },
            verticalAlignment =  Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onBack() },
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Sharp.ArrowBack,
                    contentDescription = "Back to main search view"
                )
            }
            TextField(
                placeholder = { Text("Search") },
                value = state.query,
                onValueChange = { onEvent(SearchEvents.Search(it)) },
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedIndicatorColor = MaterialTheme.colorScheme.background,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.background
                )
            )
        }
        LazyColumn(modifier = Modifier.systemBarsPadding()) {
            items(state.searchResults) { music ->

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

                    IconButton(
                        onClick = { onEvent(SearchEvents.OnFavoriteToggle(music)) }
                    ) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = if (music.isFavorite) Icons.Rounded.CheckCircle else Icons.Rounded.AddCircle,
                            contentDescription = if (music.isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (music.isFavorite) Color(0xFF2ECC71) else MaterialTheme.colorScheme.onSurfaceVariant
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
