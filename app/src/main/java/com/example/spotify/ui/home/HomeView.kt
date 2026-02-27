package com.example.spotify.ui.home

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.spotify.ui.component.permission.PermissionRequired
import com.example.spotify.ui.component.permission.rememberPermissionState

import kotlinx.coroutines.launch


@Composable
fun HomeView(state: HomeViewState, onEvent: (HomeViewEvents) -> Unit){

    val permission = if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val mediaPermissionState = rememberPermissionState(permission)

    PermissionRequired(
        permissionState = mediaPermissionState,
        notGrantedContent = {
            // User hali ruxsat ham bermagan rad ham etmagan
            // Bu yerda musiqa playeri uchun permission kerak degan UI qilish kerak
            // Ruxsat berish degan tugma qo'shish kerak
            Column(modifier = Modifier.systemBarsPadding()){
                Text("Need permission to access music")
                Button(
                    onClick = { mediaPermissionState.launchPermissionRequest() }
                ) {
                    Text("Grand access")
                }
            }
        },
        deniedContent = {
            // Bu yerda user permission rad etgan
            // Bu yerda Iltimos settingga o'tib permission bering deb yozish kerak
            // Settingga o'tish degan tugma turadi
            Column(modifier = Modifier.systemBarsPadding()){
                Text("Permissions rejected, try again")
                Button(
                    onClick = { mediaPermissionState.launchPermissionRequest() }
                ) {
                    Text("Retry")
                }
            }
        },
        grantedContent = {
            // Bu Yerda permission bor bo'lsa ishlaydi
            LaunchedEffect("true") {
                onEvent(HomeViewEvents.PermissionGranted)
            }

            LazyColumn(modifier = Modifier.systemBarsPadding()){
                items(state.musics.size){
                    Text(state.musics[it].name ?: "null")
                    Text(state.musics[it].duration.toString())
                    Text( state.musics[it].filePath ?: "null")
                    Text( state.musics[it].id.toString())
                    Text( "********")
                }
            }
        }
    )
}