package com.example.spotify.player

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerManagerViewModel @Inject constructor(
    playerManager: PlayerManager
) : ViewModel() {
    val state = playerManager.playerState
}