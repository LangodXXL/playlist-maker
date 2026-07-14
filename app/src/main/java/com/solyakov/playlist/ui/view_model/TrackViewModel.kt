package com.solyakov.playlist.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.data.playlist.Playlist
import com.solyakov.playlist.domain.player.TrackPlayer
import com.solyakov.playlist.domain.repository.PlaylistsRepository
import com.solyakov.playlist.domain.repository.TracksRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


sealed class TrackScreenState {
    object Loading: TrackScreenState()
    data class Error(val message: String): TrackScreenState()
    data class Success(val track: Track): TrackScreenState()
}

class TrackViewModel(
    private val playlistRepository: PlaylistsRepository,
    private val trackRepository: TracksRepository,
    private val trackPlayer: TrackPlayer
): ViewModel() {


    val playerState = trackPlayer.state
    private val _screenState = MutableStateFlow<TrackScreenState>(TrackScreenState.Loading)
    val screenState = _screenState.asStateFlow()
    private val _track = MutableStateFlow<Track?>(null)
    val track = _track.asStateFlow()

    private var timerJob: Job? = null


    private fun stopTimer() {
        timerJob?.cancel()
    }

    fun playTrack() {
        trackPlayer.togglePlayPause()
    }

    fun playNext() {
        trackPlayer.seekToNext()
    }

    fun playPrevious() {
        trackPlayer.seekToPrevious()
    }

    fun seekTo(position: Float) {
        trackPlayer.seekTo(position.toLong())
    }

    fun addTrackToFavorite(track: Track) {
        viewModelScope.launch {
            trackRepository.updateTrackFavoriteStatus(track)
            val updatedTrack = track.copy(favorite = !track.favorite)
            _track.update { updatedTrack }
            _screenState.update { TrackScreenState.Success(updatedTrack) }
        }
    }

    fun addTrackInPlaylist(track: Track, playlistId: Long) {
        viewModelScope.launch {
            trackRepository.insertTrackToPlaylist(track, playlistId)
        }
    }


    fun getAllPlaylists(): Flow<List<Playlist>> {
           return playlistRepository.getAllPlaylists()
    }

    fun getTrackById(trackId: Long) {
        _screenState.update { TrackScreenState.Loading }
        viewModelScope.launch {
            try {
                val response = trackRepository.getTrackById(trackId)
                _track.update { response }
                _screenState.update { TrackScreenState.Success(response) }
            } catch (e: Exception) {
                _screenState.update { TrackScreenState.Error(e.message ?: "Unknown exception") }

            }
        }
    }
}