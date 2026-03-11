package com.solyakov.playlist.domain.repository

import com.solyakov.playlist.data.network.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>

    suspend fun getTrackById(trackId: Long): Track
    suspend fun updateTrackFavoriteStatus(track: Track)
    suspend fun deleteTrackFromAllPlaylists(track: Track)

    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long)
    suspend fun insertTrackToPlaylist(track: Track, playlistId: Long)
    fun getFavoriteTracks(): Flow<List<Track>>
}

