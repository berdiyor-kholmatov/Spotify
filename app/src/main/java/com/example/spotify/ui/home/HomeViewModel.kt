package com.example.spotify.ui.home

import android.content.ContentResolver
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.spotify.domain.model.MusicFile
import com.example.spotify.player.PlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



//class MusicViewModel(
//    application: Application
//) : AndroidViewModel(application) {
//
//    private val player = ExoPlayer.Builder(application).build()
//
//    fun play(url: String) {
//        val mediaItem = MediaItem.fromUri(url)
//        player.setMediaItem(mediaItem)
//        player.prepare()
//        player.play()
//    }
//
//    override fun onCleared() {
//        super.onCleared()
//        player.release()
//    }
//}




@HiltViewModel
class HomeViewModel @Inject constructor(
    private var contentResolver: ContentResolver?,
    private val playerManager: PlayerManager
) : ViewModel() {

    private val _state = MutableStateFlow(HomeViewState())
    val state = _state.asStateFlow()

    private suspend fun loadFiles () = viewModelScope.launch(Dispatchers.Default){
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

                _state.value = _state.value.copy(musics = musicFile)
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
    }


    fun onEvent(event: HomeViewEvents) {
        when(event) {
            is HomeViewEvents.OnMusicSelected -> {
                event.music.filePath?.let{
                    _state.value = _state.value.copy(selectedMusic = event.music, isPlaying = true)
                    play(event.music.filePath)
                }
            }

            HomeViewEvents.OnPlayPauseClicked -> {

            }

            HomeViewEvents.OnSkipNextClicked -> {

            }

            HomeViewEvents.OnSkipPreviousClicked -> {

            }

            HomeViewEvents.OnShuffleRepeatLoopClicked -> {

            }
            HomeViewEvents.PermissionGranted -> {
                viewModelScope.launch {
                    loadFiles()
                }
            }
        }
    }

    fun play(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        playerManager.player.setMediaItem(mediaItem)
        playerManager.player.prepare()
        playerManager.player.play()
    }



}
