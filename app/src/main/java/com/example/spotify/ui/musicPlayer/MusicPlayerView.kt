package com.example.spotify.ui.musicPlayer

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.spotify.ui.home.getAlbumArtUri

@Composable
fun MusicPlayerView(context: Context, state: MusicPlayerViewState, onEvent: (MusicPlayerViewEvents) -> Unit) {

    Box (
        modifier = Modifier
            .systemBarsPadding()
    ) {

        // implementation of the spotify like fade
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            state.dominantColor.copy(alpha = 1f),
                            Color.Black.copy(alpha = 1f)
                        )
                    )
                )
        )


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

        }

    }




}