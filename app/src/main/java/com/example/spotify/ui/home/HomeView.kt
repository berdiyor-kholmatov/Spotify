package com.example.spotify.ui.home

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.spotify.domain.model.MusicFile
import com.example.spotify.service.playerService.PlayerService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState



data class BottomNavigationItem (
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    )

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeView(state: HomeViewState, onEvent: (HomeViewEvents) -> Unit){

    val context = LocalContext.current
    val permissions = if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.POST_NOTIFICATIONS
        )
    } else {
        listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    val multiplePermissionsState = rememberMultiplePermissionsState(permissions)

    if (multiplePermissionsState.allPermissionsGranted) {
        LaunchedEffect("true") {
            onEvent(HomeViewEvents.PermissionGranted)
        }

        LazyColumn(modifier = Modifier.systemBarsPadding()) {
            items(state.musics) { music ->

                Row (
                    modifier = Modifier
                        .padding(horizontal = 12.dp,vertical = 8.dp)
                        .clickable {
                            onEvent(HomeViewEvents.OnMusicSelected(music))
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

                    //MusicCell(music)
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
/*
//    PermissionRequired(
//        permissionState = mediaPermissionState,
//        notGrantedContent = {
//            // User hali ruxsat ham bermagan rad ham etmagan
//            // Bu yerda musiqa playeri uchun permission kerak degan UI qilish kerak
//            // Ruxsat berish degan tugma qo'shish kerak
//            Column(modifier = Modifier.systemBarsPadding()){
//                Text("Need permission to access music")
//                Button(
//                    onClick = { mediaPermissionState.launchPermissionRequest() }
//                ) {
//                    Text("Grand access")
//                }
//            }
//        },
//        deniedContent = {
//            // Bu yerda user permission rad etgan
//            // Bu yerda Iltimos settingga o'tib permission bering deb yozish kerak
//            // Settingga o'tish degan tugma turadi
//            Column(modifier = Modifier.systemBarsPadding()){
//                Text("Permissions rejected, try again")
//                Button(
//                    onClick = { mediaPermissionState.launchPermissionRequest() }
//                ) {
//                    Text("Retry")
//                }
//            }
//        },
//        grantedContent = {
//            // Bu Yerda permission bor bo'lsa ishlaydi
//            LaunchedEffect("true") {
//                onEvent(HomeViewEvents.PermissionGranted)
//            }
//
//            Scaffold(
//                modifier = Modifier.fillMaxSize(),
//                bottomBar = {
//                    if (state.selectedMusic != null) {
//                        MiniPlayer(state, onEvent)
//                    }
//                }
//            ) { padding ->
//                LazyColumn(modifier = Modifier.systemBarsPadding()) {
//                    items(state.musics) { music ->
//                        TextButton(
//                            onClick = { onEvent(HomeViewEvents.OnMusicSelected( music)) },
//                            content = { MusicCell(music) },
//                            modifier = Modifier.fillMaxWidth()
//                                .background(MaterialTheme.colorScheme.secondaryContainer)
//                        )
//
//                        Spacer(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(1.dp)
//                                .padding(horizontal = 6.dp)
//                                .background(
//                                    MaterialTheme.colorScheme.onSecondaryContainer.copy(
//                                        alpha = 0.8f
//                                    )
//                                )
//                        )
//                    }
//                }
//            }
//        }
//    )
    */
}



@Composable
fun MiniPlayer(context: Context, state: HomeViewState, onEvent: (HomeViewEvents) -> Unit) {

    Row(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(state.dominantColor)
            .clickable { },
        verticalAlignment =  Alignment.CenterVertically
    ) {

        AsyncImage(
            model = getAlbumArtUri(state.selectedMusic?.albumId ?: 0),
            contentDescription = null,
            modifier = Modifier
                .padding(6.dp)
                .width(50.dp)
                .height(50.dp)
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
                onEvent(HomeViewEvents.OnSkipPreviousClicked)
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
                onEvent(HomeViewEvents.OnPlayPauseClicked)
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
                onEvent(HomeViewEvents.OnSkipNextClicked)
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

}

@Composable
fun MusicCell(music: MusicFile) {
        Column ( modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()

        ) {
            Text(
                music.name ?: "null",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                music.artist ?: "null",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
}


fun getAlbumArtUri(albumId: Long): Uri {
    return ContentUris.withAppendedId(
        Uri.parse("content://media/external/audio/albumart"),
        albumId
    )
}
