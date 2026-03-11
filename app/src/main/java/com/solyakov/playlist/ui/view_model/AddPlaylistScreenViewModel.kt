package com.solyakov.playlist.ui.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solyakov.playlist.data.playlist.ImageSaver
import com.solyakov.playlist.domain.repository.PlaylistsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddPlaylistScreenViewModel(
    private val playlistsRepository: PlaylistsRepository,
    private val imageSaver: ImageSaver
): ViewModel() {
    private val _playlistName = MutableStateFlow("")
    val playlistName = _playlistName.asStateFlow()

    private val _playlistDescription = MutableStateFlow("")
    val playlistDescription = _playlistDescription.asStateFlow()

    private val _selectedImage = MutableStateFlow<Uri?>(null)
    val selectedImage = _selectedImage.asStateFlow()


    fun setSelectedImage(uri: Uri?) {
        _selectedImage.update { uri }
    }

    fun setPlaylistName(name: String) {
        _playlistName.update { name }
    }
    fun setPlaylistDescription(description: String) {
        _playlistDescription.update { description }
    }

    fun savePlaylist() {
        viewModelScope.launch {
            val uri = _selectedImage.value
            val image = if (selectedImage.value != null) {
                imageSaver.saveImageToInternalStorage(uri.toString())
            } else null
            playlistsRepository.addPlaylist(
                name = _playlistName.value,
                description = _playlistDescription.value,
                image = image
            )
        }

    }
}





