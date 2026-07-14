package com.solyakov.playlist.domain.player

import com.solyakov.playlist.data.network.Track

data class PlayerState(
    val currentTrack: Track? = null,
    val isPlaying: Boolean = false,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val currentIndex: Int = 0,
    val hasNext: Boolean = false,
    val hasPrevious: Boolean = false
)