package com.solyakov.playlist.domain.player

import com.solyakov.playlist.data.network.Track
import kotlinx.coroutines.flow.StateFlow

interface TrackPlayer {

    val state: StateFlow<PlayerState>

    fun setQueue(
        tracks: List<Track>,
        startIndex: Int,
        playWhenReady: Boolean = false
    )

    fun togglePlayPause()

    fun play()
    fun pause()
    fun seekTo(positionMs: Long)
    fun seekToNext()
    fun seekToPrevious()
    fun stop()
}