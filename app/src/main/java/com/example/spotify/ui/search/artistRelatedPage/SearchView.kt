package com.example.spotify.ui.search.artistRelatedPage

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import com.example.spotify.ui.home.getAlbumArtUri


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(state: SearchViewState, onEvent: (SearchViewEvents) -> Unit) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(Color.Black),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.background(Color.Black),
                title = {
                    Text(
                        text = "Search",
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
                        onClick = { /*TODO*/ },
                    ) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            imageVector = Icons.Sharp.Add,
                            contentDescription = "Add new library"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Black,              // фон
                    scrolledContainerColor = Color.Black       // при скролле (ВАЖНО)
                )
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



                    stickyHeader {
                        SearchBarButton( modifier = Modifier.padding(horizontal = 6.dp), onClick = { /*TODO*/ })
                    }

                    val grouped = state.musics.groupBy { it.artist }

//                    grouped.forEach { (artist, items) ->

                        items(grouped.size) { index ->
                            val bitmap = getAlbumArtBitmap(LocalContext.current, grouped[grouped.keys.elementAt(index).toString()]?.first()?.albumId)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(115.dp)
                                    .padding(6.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(darken(bitmap?.let { extractDominantColor(it)} ?: Color.Gray))
                                    .clickable(
                                        onClick = { /*TODO*/ }
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
                                            grouped.keys.elementAt(index).toString(),
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
                                        model = getAlbumArtUri(grouped[grouped.keys.elementAt(index).toString()]?.first()?.albumId ?: 0),
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