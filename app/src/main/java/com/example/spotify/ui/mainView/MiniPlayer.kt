package com.example.spotify.ui.mainView

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.spotify.player.PlayerState
import com.example.spotify.service.playerService.PlayerService
import com.example.spotify.ui.home.getAlbumArtUri

@Composable
fun MiniPlayer(context: Context, state: PlayerState, onClick: () -> Unit) {

    Box ( ) {
        Row(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(state.dominantColor)
                .clickable { onClick() },
            verticalAlignment =  Alignment.CenterVertically
        ) {

            AsyncImage(
                model = getAlbumArtUri(state.selectedMusic?.albumId ?: 0),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .width(48.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(MaterialTheme.colorScheme.error)
                ,
                contentScale = ContentScale.Crop
            )

            Column ( modifier = Modifier
                .height(56.dp)
                .weight(1f)
                .padding(4.dp)
                .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    state.selectedMusic?.name ?: "null",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontWeight =  FontWeight.Bold,
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
                    Intent(context, PlayerService::class.java).also {
                        it.action = PlayerService.Actions.PREVIOUS.toString()
                        context.startService(it)
                    }
                }
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
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
                }
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector =  if (!state.isPlaying) Icons.Rounded.PlayCircle else Icons.Rounded.PauseCircle,
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
                }
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Rounded.SkipNext,
                    contentDescription = null,
                    tint = Color.White,
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
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
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
            .height(2.dp) // 👈 ВОТ ТВОЙ КОНТРОЛЬ ТОЛЩИНЫ
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
