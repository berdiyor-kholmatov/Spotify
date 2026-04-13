package com.example.spotify.ui.main.artist

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.spotify.ui.main.home.getAlbumArtUri


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Artist( artist: String, state: ArtistState, onEvent: (ArtistEvents) -> Unit) {

    val context = LocalContext.current

    LazyColumn(modifier = Modifier.systemBarsPadding()) {
        items(state.artistsMusic) { music ->

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
                    onClick = { onEvent(ArtistEvents.OnFavoriteToggle(music)) }
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


@Composable
fun SearchBarButton( modifier: Modifier, onClick: () -> Unit) {
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.Black)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(Color.White.copy(alpha = 0.99f))
                .clickable { onClick() }
                .padding(horizontal = 17.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Search",
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                fontWeight = FontWeight.Bold,
            )
        }
    }

}


fun getAlbumArtUri(albumId: Long): Uri {
    return ContentUris.withAppendedId(
        Uri.parse("content://media/external/audio/albumart"),
        albumId
    )
}

fun getAlbumArtBitmap(context: Context, albumId: Long?): Bitmap? {
    if (albumId == null) return null

    return try {
        val uri = getAlbumArtUri(albumId)
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        null
    }
}

fun extractDominantColor(bitmap: Bitmap): Color {
    val palette = Palette.from(bitmap).generate()

    val color = palette.getDominantColor(android.graphics.Color.GRAY)

    return Color(color)
}

fun darken(color: Color, factor: Float = 0.7f): Color {
    return Color(
        red = color.red * factor,
        green = color.green * factor,
        blue = color.blue * factor,
        alpha = 1f
    )
}
