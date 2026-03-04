package com.example.spotify.ui.home

import android.Manifest
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.spotify.R
import com.example.spotify.domain.model.MusicFile
import com.example.spotify.ui.component.permission.PermissionRequired
//import com.example.spotify.ui.component.permission.PermissionRequired
//import com.example.spotify.ui.component.permission.rememberPermissionState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

import kotlinx.coroutines.launch


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeView(state: HomeViewState, onEvent: (HomeViewEvents) -> Unit){

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

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (state.selectedMusic != null) {
                    MiniPlayer(state, onEvent)
                }
            }
        ) { padding ->
            LazyColumn(modifier = Modifier.systemBarsPadding()) {
                items(state.musics) { music ->
                    TextButton(
                        onClick = { onEvent(HomeViewEvents.OnMusicSelected(music)) },
                        content = { MusicCell(music) },
                        modifier = Modifier.fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                    )

                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(horizontal = 6.dp)
                            .background(
                                MaterialTheme.colorScheme.onSecondaryContainer.copy(
                                    alpha = 0.8f
                                )
                            )
                    )
                }
            }
        }
    }






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
}



@Composable
fun MiniPlayer(state: HomeViewState, onEvent: (HomeViewEvents) -> Unit) {

    Row(
        modifier = Modifier.padding(8.dp)
            .systemBarsPadding()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text("${state.selectedMusic?.name}", modifier = Modifier.weight(1f))
        IconButton(
//                                modifier = Modifier
//                                    .size(56.dp)
//                                    .padding(16.dp),
            onClick = { onEvent(HomeViewEvents.OnSkipPreviousClicked) }
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.skip_previous_24dp),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }

        IconButton(
//                                modifier = Modifier
//                                    .size(56.dp)
//                                    .padding(16.dp),
            onClick = { onEvent(HomeViewEvents.OnPlayPauseClicked) }
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = if (!state.isPlaying) R.drawable.play_circle_24dp else R.drawable.pause_circle_24dp),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }

        IconButton(
//                                modifier = Modifier
//                                    .size(56.dp)
//                                    .padding(16.dp),
            onClick = { onEvent(HomeViewEvents.OnSkipNextClicked) }
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = R.drawable.skip_next_24dp),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }

}

@Composable
fun MusicCell(music: MusicFile) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(music.name ?: "null")
        Text(music.duration.toString())
        Text( music.filePath ?: "null")
        Text( music.id.toString())
    }
}

