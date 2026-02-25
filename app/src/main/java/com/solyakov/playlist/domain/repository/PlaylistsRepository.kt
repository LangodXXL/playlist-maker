package com.solyakov.playlist.domain.repository

import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.data.playlist.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    fun getAllPlaylists(): Flow<List<Playlist>>

    fun addPlaylist(name: String, description: String)

    suspend fun deletePlaylist(playlistId: Long)

    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long)

    fun getPlaylist(id: Long): Flow<Playlist?>

    fun getFavoriteTracks(): Flow<List<Track>>

    fun insertTrackToPlaylist(track: Track, playlistId: Long)

    suspend fun toggleFavorite(track: Track)

    suspend fun deletePlaylistById(id: Long)
}




