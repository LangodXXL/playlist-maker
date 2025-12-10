package com.solyakov.playlist.domain.api

import com.solyakov.playlist.domain.models.Track

interface TrackSearchInteractor {
    fun searchTracks(expression: String): List<Track>
}