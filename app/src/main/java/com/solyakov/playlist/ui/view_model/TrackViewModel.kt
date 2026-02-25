package com.solyakov.playlist.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.domain.repository.PlaylistsRepository
import kotlinx.coroutines.launch

class TrackViewModel(
    val track: Track,
    val playlistRepository: PlaylistsRepository
): ViewModel() {
    val scope = viewModelScope
    fun addTrackToFavorite(track: Track) {
        scope.launch {
            playlistRepository.toggleFavorite(track)
        }
    }

    fun addTrackInPlaylist(track: Track, playlistId: Long) {
        scope.launch {
            playlistRepository.insertTrackToPlaylist(track, playlistId)
        }
    }

}