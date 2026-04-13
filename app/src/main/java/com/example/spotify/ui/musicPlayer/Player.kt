package com.example.spotify.ui.musicPlayer

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spotify.service.playerService.PlayerService
import com.example.spotify.ui.main.home.getAlbumArtUri

@Composable
fun MusicPlayerView(state: PlayerState, onEvent: (PlayerEvents) -> Unit) {

    val context = LocalContext.current

    Box (
        modifier = Modifier
            .fillMaxSize()
            //.align(Alignment.TopCenter)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        state.dominantColor.copy(alpha = 1f),
                        Color.Black.copy(alpha = 1f)
                    )
                )
            )
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent) // 🔥 ВАЖНО
        ) {
            AsyncImage(
                model = getAlbumArtUri(state.selectedMusic?.albumId ?: 0),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(30.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.error)

                ,
                contentScale = ContentScale.Crop
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(104.dp)
                    .padding(start = 16.dp, top = 27.dp, end = 16.dp, bottom = 27.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column (
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        state.selectedMusic?.name ?: "null",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        fontWeight =  FontWeight.Bold,
                        fontSize = 20.sp,
                        maxLines = 1,
                    )
                    Text(
                        state.selectedMusic?.artist ?: "null",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }

                IconButton(
                    onClick = {
                        state.selectedMusic?.let {
                            onEvent(PlayerEvents.OnFavoriteToggle(it))
                        }
                    },
                    modifier = Modifier.size(35.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = if(state.selectedMusic?.isFavorite == true) Icons.Rounded.CheckCircle else Icons.Rounded.AddCircleOutline,
                        contentDescription = null,
                        tint = if(state.selectedMusic?.isFavorite == true) Color.Green else Color.White,
                    )
                }
            }

            MusicProgressBar(
                progress = state.currentPosition.toFloat() / (state.selectedMusic?.duration?.toFloat() ?: 1f),
                onSeek = { newProgress ->
                    val newPosition = ((state.selectedMusic?.duration?.toFloat() ?: 1f) * newProgress).toLong()
                    Intent(context, PlayerService::class.java).apply {
                        action = "SEEK"
                        putExtra("POSITION", newPosition.toLong())
                    }.also {
                        context.startService(it)
                    }
                },
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
            )

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                IconButton(
                    onClick = {
                        onEvent(PlayerEvents.OnShuffleClicked)
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        imageVector = Icons.Rounded.Shuffle,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }

                IconButton(
                    onClick = {
                        Intent(context, PlayerService::class.java).also {
                            it.action = PlayerService.Actions.PREVIOUS.toString()
                            context.startService(it)
                        }
                    },
                    modifier = Modifier.size(45.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(45.dp),
                        imageVector = Icons.Rounded.SkipPrevious,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }

                IconButton(
                    onClick = {
                        Intent(context, PlayerService::class.java).also {
                            it.action = if (state.isPlaying) PlayerService.Actions.PAUSE.toString() else PlayerService.Actions.RUN.toString()
                            context.startService(it)
                        }
                    },
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(80.dp),
                        imageVector = if (state.isPlaying) Icons.Rounded.PauseCircle else Icons.Rounded.PlayCircle,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }

                IconButton(
                    onClick = {
                        Intent(context, PlayerService::class.java).also {
                            it.action = PlayerService.Actions.NEXT.toString()
                            context.startService(it)
                        }
                    },
                    modifier = Modifier.size(45.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(45.dp),
                        imageVector = Icons.Rounded.SkipNext,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }

                IconButton(
                    onClick = {

                    }
                ) {
                    Icon(
                        modifier = Modifier.size(25.dp),
                        imageVector = if(state.isPlaying) Icons.Rounded.Repeat else Icons.Rounded.RepeatOne,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }
            }
        }
    }
}


@Composable
fun MusicProgressBar(
    progress: Float, // 0f..1f
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(3.dp) // 👈 ВОТ ТВОЙ КОНТРОЛЬ ТОЛЩИНЫ
            .clip(RoundedCornerShape(50))
            .background(Color.Gray.copy(alpha = 0.3f))
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val newProgress = offset.x / size.width
                    onSeek(newProgress.coerceIn(0f, 1f))
                }
            },
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress)
                .background(Color.White)
        )
    }
}
