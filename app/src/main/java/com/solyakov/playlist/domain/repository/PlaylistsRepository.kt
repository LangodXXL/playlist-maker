package com.solyakov.playlist.domain.repository

import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.data.playlist.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun addPlaylist(name: String, description: String, image: String?)

    suspend fun deletePlaylist(playlistId: Long)

    suspend fun getPlaylist(id: Long): Playlist

    fun getCountTracksInPlaylist(playlistId: Long): Flow<Int>
    fun getAllTrackInPlaylist(playlistId: Long): Flow<List<Track>>
}




