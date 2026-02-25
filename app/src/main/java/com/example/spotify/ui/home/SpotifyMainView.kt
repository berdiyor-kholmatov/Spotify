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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.spotify.ui.component.permission.PermissionRequired
import com.example.spotify.ui.component.permission.rememberPermissionState

import kotlinx.coroutines.launch
import kotlin.collections.get

@Composable
fun SpotifyMainView(musicViewModel: SpotifyViewModel){

    val permission = if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val mediaPermissionState = rememberPermissionState(permission)

    val scope = rememberCoroutineScope()
    val list by musicViewModel.musicList.collectAsStateWithLifecycle(listOf())

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
            LaunchedEffect(true) {
                scope.launch {
                    musicViewModel.loadFiles()
                }
            }

            LazyColumn(modifier = Modifier.systemBarsPadding()){
                items(list.size){
                    Text(list[it].name ?: "null")
                    Text(list[it].duration.toString())
                    Text( list[it].filePath ?: "null")
                    Text( list[it].id.toString())
                    Text( "********")
                }
            }
        }
    )
}