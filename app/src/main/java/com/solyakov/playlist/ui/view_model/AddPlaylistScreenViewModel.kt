package com.solyakov.playlist.ui.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solyakov.playlist.domain.repository.PlaylistsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddPlaylistScreenViewModel(
    private val playlistsRepository: PlaylistsRepository
): ViewModel() {
    private val _playlistName = MutableStateFlow("")
    val playlistName = _playlistName.asStateFlow()

    private val _playlistDescription = MutableStateFlow("")
    val playlistDescription = _playlistDescription.asStateFlow()


    fun setPlaylistName(name: String) {
        _playlistName.update { name }
    }
    fun setPlaylistDescription(description: String) {
        _playlistDescription.update { description }
    }

    fun savePlaylist() {
        viewModelScope.launch {
            playlistsRepository.addPlaylist(
                name = _playlistName.value,
                description = _playlistDescription.value
            )
        }

    }
}





