package com.example.spotify.ui.theme.mainUI

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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SpotifyMainView(musicViewModel: SpotifyViewModel){

    var permition = android.Manifest.permission.READ_EXTERNAL_STORAGE

    if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permition = android.Manifest.permission.READ_MEDIA_AUDIO
    } else {
        permition = android.Manifest.permission.READ_EXTERNAL_STORAGE
    }
    val mediaPermissionState = rememberPermissionState(permition){
        if (it){
            musicViewModel.setPermission()
        }
    }

    val scope = rememberCoroutineScope()
    val list by musicViewModel.musicList.collectAsStateWithLifecycle(listOf())

    if(musicViewModel.permissionGranted.collectAsState().value){

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
    } else {
        Column(modifier = Modifier.systemBarsPadding()){
            Text("Permissions rejected, try again")
            Button(
                onClick = { mediaPermissionState.launchPermissionRequest() }
            ) {
                Text("Retry")
            }
        }
    }


}