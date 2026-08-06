package com.solyakov.playlist.testutil

import com.solyakov.playlist.domain.models.Track
import com.solyakov.playlist.domain.player.PlayerState
import com.solyakov.playlist.domain.player.TrackPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeTrackPlayer : TrackPlayer {

    private val _state = MutableStateFlow(PlayerState())
    override val state: StateFlow<PlayerState> = _state

    var queue: List<Track> = emptyList()
        private set
    var startIndex: Int? = null
        private set
    var playWhenReady: Boolean? = null
        private set

    var togglePlayPauseCalled = false
        private set
    var playCalled = false
        private set
    var pauseCalled = false
        private set
    var stopCalled = false
        private set
    var seekToPositionMs: Long? = null
        private set
    var seekToNextCalled = false
        private set
    var seekToPreviousCalled = false
        private set

    override fun setQueue(
        tracks: List<Track>,
        startIndex: Int,
        playWhenReady: Boolean
    ) {
        queue = tracks
        this.startIndex = startIndex
        this.playWhenReady = playWhenReady

        _state.value = _state.value.copy(
            currentTrack = tracks.getOrNull(startIndex),
            currentIndex = startIndex,
            isPlaying = playWhenReady,
            hasPrevious = startIndex > 0,
            hasNext = startIndex < tracks.lastIndex
        )
    }

    override fun togglePlayPause() {
        togglePlayPauseCalled = true
        _state.value = _state.value.copy(isPlaying = !_state.value.isPlaying)
    }

    override fun play() {
        playCalled = true
        _state.value = _state.value.copy(isPlaying = true)
    }

    override fun pause() {
        pauseCalled = true
        _state.value = _state.value.copy(isPlaying = false)
    }

    override fun seekTo(positionMs: Long) {
        seekToPositionMs = positionMs
        _state.value = _state.value.copy(positionMs = positionMs)
    }

    override fun seekToNext() {
        seekToNextCalled = true
    }

    override fun seekToPrevious() {
        seekToPreviousCalled = true
    }

    override fun stop() {
        stopCalled = true
        _state.value = PlayerState()
    }
}
