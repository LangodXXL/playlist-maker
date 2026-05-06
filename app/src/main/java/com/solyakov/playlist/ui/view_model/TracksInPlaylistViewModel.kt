package com.solyakov.playlist.ui.view_model

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.domain.repository.PlaylistsRepository
import com.solyakov.playlist.domain.repository.TracksRepository
import com.solyakov.playlist.toMediaItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TracksInPlaylistViewModel(
    private val playlistsRepository: PlaylistsRepository,
    context: Context,
    sessionToken: SessionToken
): ViewModel() {

    private var controller: MediaController? = null

    init {
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({
            controller = controllerFuture.get()
        },
            MoreExecutors.directExecutor())
    }

    fun onTrackClick(tracks: List<Track>, startIndex: Int) {
        val controller = controller ?: return
        val mediaItems = tracks.map { it.toMediaItem() }

        controller.setMediaItems(mediaItems, startIndex, 0L)
        controller.prepare()
    }

    override fun onCleared() {
        super.onCleared()
        controller?.release()
        controller = null
    }

    private val _tracks = MutableStateFlow<List<Track>>(emptyList())
    val tracks = _tracks.asStateFlow()

    val tracksCount = tracks.map {
        it.size
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0
    )


    fun getAllTracksInPlaylist(playlistId: Long) {
        viewModelScope.launch {
            playlistsRepository.getAllTrackInPlaylist(playlistId).collect {newList ->
                Log.d("TracksInPlaylistViewModel", "Tracks: $newList")
                _tracks.update {
                    newList
                }
            }
        }
    }
}