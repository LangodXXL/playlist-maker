package com.solyakov.playlist.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.domain.repository.PlaylistsRepository
import com.solyakov.playlist.domain.repository.TracksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException


sealed class TrackScreenState {
    class Loading: TrackScreenState()
    class Error(val message: String): TrackScreenState()
    class Success(val track: Track): TrackScreenState()
}

class TrackViewModel(
    val playlistRepository: PlaylistsRepository,
    val trackRepository: TracksRepository
): ViewModel() {
    private val _screenState = MutableStateFlow<TrackScreenState>(TrackScreenState.Loading())
    val screenState = _screenState.asStateFlow()

    private val _track = MutableStateFlow<Track?>(null)
    fun getTrack() = _track.asStateFlow()

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

    fun getTrackById(trackId: Long) {
        try {
            _screenState.update { TrackScreenState.Loading() }
            viewModelScope.launch {
                val response = trackRepository.getTrackById(trackId)
                _track.update { response }
                _screenState.update { TrackScreenState.Success(response) }
            }
        } catch (e: IOException) {
            _screenState.update { TrackScreenState.Error(e.message ?: "Unknown exception") }
        }
    }

}