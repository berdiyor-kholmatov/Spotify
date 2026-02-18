package com.example.spotify.ui.theme.mainUI

import android.content.ContentResolver
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SpotifyViewModel: ViewModel() {

    private var contentResolver: ContentResolver? = null

    private val _musicList = MutableStateFlow(listOf<MusicFile>())
    val musicList: StateFlow<List<MusicFile>> = _musicList

    private val _permissionGranted = MutableStateFlow(false)
    val permissionGranted: StateFlow<Boolean> = _permissionGranted


    fun setPermission() {
        _permissionGranted.value = true
    }


    suspend fun loadFiles () = viewModelScope.launch(Dispatchers.Default){
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} DESC"

        contentResolver?.let { consRes ->
            val cursor = consRes.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                sortOrder
            )


            val musicFile = mutableListOf<MusicFile>()
            cursor?.use {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val filePathColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

                while (it.moveToNext()){
                    val id = it.getLong(idColumn)
                    val name = it.getString(nameColumn)
                    val duration = it.getLong(durationColumn)
                    val filePath = it.getString(filePathColumn)

                    musicFile.add(MusicFile(id, name, duration, filePath))
                }

                _musicList.value = musicFile
            }
        }
    }

    fun setContentResolver(contentResolver: ContentResolver?) {
        this.contentResolver = contentResolver
    }


    override fun onCleared() {
        super.onCleared()
    }


}

data class MusicFile(
    val id: Long? = null,
    val name: String? = null,
    val duration: Long? = null,
    val filePath: String? = null,
)