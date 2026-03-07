package com.solyakov.playlist.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.domain.repository.PlaylistsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TracksInPlaylistViewModel(
    private val playlistsRepository: PlaylistsRepository
): ViewModel() {

    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks = _tracks.asStateFlow()


    fun getAllTracksInPlaylist(playlistId: Long) {
        viewModelScope.launch {
            _tracks.update {
                playlistsRepository.getAllTrackInPlaylist(playlistId)
            }
        }
    }
}