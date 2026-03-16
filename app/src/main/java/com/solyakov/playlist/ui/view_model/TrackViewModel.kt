package com.solyakov.playlist.ui.view_model

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.data.playlist.Playlist
import com.solyakov.playlist.domain.repository.PlaylistsRepository
import com.solyakov.playlist.domain.repository.TracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import androidx.media3.common.Player
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay


sealed class TrackScreenState {
    class Loading: TrackScreenState()
    class Error(val message: String): TrackScreenState()
    class Success(val track: Track): TrackScreenState()
}

class TrackViewModel(
    context: Context,
    val playlistRepository: PlaylistsRepository,
    val trackRepository: TracksRepository,
    sessionToken: SessionToken
): ViewModel() {

    private var controller: MediaController? = null

    val isPlaying = mutableStateOf(false)
    val currentPosition = mutableStateOf(0f)
    val duration = mutableStateOf(30000f)

    private var timerJob: Job? = null
    init {
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({
            val ctrl = controllerFuture.get()
            controller = ctrl
            isPlaying.value = ctrl.isPlaying
            ctrl.currentMediaItem?.let { item ->
                getTrackById(item.mediaId.toLong())
            }

            ctrl.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(playing: Boolean) {
                    isPlaying.value = playing
                    if (playing) startTimer() else stopTimer()
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY) {
                        duration.value = ctrl.duration.toFloat()
                    }
                }
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    mediaItem?.let {
                        getTrackById(it.mediaId.toLong())
                    }
                }
            })
        }, MoreExecutors.directExecutor())
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                currentPosition.value = controller?.currentPosition?.toFloat() ?: 0f
                delay(500)
            }
        }
    }
    private fun stopTimer() {
        timerJob?.cancel()
    }

    fun playTrack(track: Track) {
        val ctrl = controller ?: return
        if (ctrl.isPlaying) {
            ctrl.pause()
        } else {
            ctrl.play()
        }
    }

    fun playNext() {
        controller?.seekToNext()
    }

    fun playPrevious() {
        controller?.seekToPrevious()
    }

    fun seekTo(position: Float) {
        controller?.seekTo(position.toLong())
    }

    override fun onCleared() {
        stopTimer()
        controller?.release()
        super.onCleared()
    }
    private val _screenState = MutableStateFlow<TrackScreenState>(TrackScreenState.Loading())
    val screenState = _screenState.asStateFlow()

    private val _track = MutableStateFlow<Track?>(null)

    val scope = viewModelScope
    fun addTrackToFavorite(track: Track) {
        scope.launch {
            trackRepository.updateTrackFavoriteStatus(track)
            val updatedTrack = track.copy(favorite = !track.favorite)
            _track.update { updatedTrack }
            _screenState.update { TrackScreenState.Success(updatedTrack) }
        }
    }

    fun addTrackInPlaylist(track: Track, playlistId: Long) {
        scope.launch {
            trackRepository.insertTrackToPlaylist(track, playlistId)
        }
    }


    fun getAllPlaylists(): Flow<List<Playlist>> {
           return playlistRepository.getAllPlaylists()
    }

    fun getTrackById(trackId: Long) {
        _screenState.update { TrackScreenState.Loading() }
        viewModelScope.launch {
            try {
                val response = trackRepository.getTrackById(trackId)
                _track.update { response }
                _screenState.update { TrackScreenState.Success(response) }
            }
         catch (e: Exception) {
            _screenState.update { TrackScreenState.Error(e.message ?: "Unknown exception") }

         }
    }
    }

}