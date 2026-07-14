package com.solyakov.playlist.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.domain.player.TrackPlayer
import com.solyakov.playlist.domain.repository.TracksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(
    private val tracksRepository: TracksRepository,
    private val trackPlayer: TrackPlayer
): ViewModel() {

    val tracks = tracksRepository.getFavoriteTracks().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )


    fun onTrackClick(tracks: List<Track>, startIndex: Int) {
        trackPlayer.setQueue(
            tracks = tracks,
            startIndex = startIndex,
            playWhenReady = false
        )
    }

    fun toggleFavorite(track: Track) {
        viewModelScope.launch {
            tracksRepository.updateTrackFavoriteStatus(track)
        }
    }
}