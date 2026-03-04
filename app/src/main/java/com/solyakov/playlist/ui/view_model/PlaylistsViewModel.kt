package com.solyakov.playlist.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.domain.repository.PlaylistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistsRepository: PlaylistsRepository
) : ViewModel() {
    val playlists = playlistsRepository.getAllPlaylists()

    fun createNewPlayList(namePlaylist: String, description: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsRepository.addPlaylist(namePlaylist, description)
        }
    }

    suspend fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        playlistsRepository.insertTrackToPlaylist(track, playlistId)
    }

    suspend fun toggleFavorite(track: Track) {
        playlistsRepository.toggleFavorite(track)
    }

    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        playlistsRepository.deleteTrackFromPlaylist(trackId, playlistId)
    }

    suspend fun deletePlaylistById(id: Long) {
        playlistsRepository.deletePlaylistById(id)
    }

}