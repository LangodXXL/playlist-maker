package com.solyakov.playlist.domain.repository

import com.solyakov.playlist.data.network.Track

interface TracksRepository {
    suspend fun getAllTracks(): List<Track>
    suspend fun searchTracks(expression: String): List<Track>
}