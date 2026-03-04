package com.solyakov.playlist.data.playlist

import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.domain.repository.PlaylistsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistsRepositoryImpl(): PlaylistsRepository {
    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())

    private val favoriteTracks = MutableStateFlow<List<Track>>(emptyList())


    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return _playlists.asStateFlow()
    }

    override fun addPlaylist(name: String, description: String) {
        _playlists.update { currentList ->
            val newPlaylist = Playlist(
                id = _playlists.value.size.toLong() + 1,
                name = name,
                description = description,
                tracks = emptyList(),
                image = null
            )
            currentList + newPlaylist
        }
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        _playlists.update { currentList ->
            currentList.filter { it.id != playlistId }
            }
    }

    override suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        _playlists.update {currentList ->
            val oldPlaylist = _playlists.value.find { it.id == playlistId } ?: return
            val updatedTracks = oldPlaylist.tracks.filter { it.trackId != trackId }
            val updatedPlaylist = oldPlaylist.copy(tracks = updatedTracks)
            currentList.map {playlist ->
                if (playlist.id == playlistId) updatedPlaylist else playlist
            }
        }
    }

    override fun getPlaylist(id: Long): Flow<Playlist?> {
        return _playlists.asStateFlow().map { playlistList ->
            playlistList.find { it.id == id}
        }
    }

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteTracks.asStateFlow()
    }



    override fun insertTrackToPlaylist(track: Track, playlistId: Long) {
        _playlists.update {
            val targetPlaylist = _playlists.value.find { it.id == playlistId  } ?: return
            if (targetPlaylist.tracks.contains(track)) return
            val updatedTracks = targetPlaylist.tracks + track
            val updatedPlaylist = targetPlaylist.copy(tracks = updatedTracks)
            val newList = _playlists.value.map {
                if (it.id == targetPlaylist.id) updatedPlaylist else it
            }
            newList
        }

    }

    override suspend fun toggleFavorite(track: Track) {
        if (track.favorite) favoriteTracks.value = favoriteTracks.value - track
        else favoriteTracks.update {
            it + track
        }
        track.favorite = !track.favorite
    }

    override suspend fun deletePlaylistById(id: Long) {
        _playlists.update { currentList ->
            currentList.filter { it.id != id }
        }
    }
}