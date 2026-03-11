package com.solyakov.playlist.ui.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.domain.repository.PlaylistsRepository
import com.solyakov.playlist.domain.repository.TracksRepository
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
    private val playlistsRepository: PlaylistsRepository
): ViewModel() {

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