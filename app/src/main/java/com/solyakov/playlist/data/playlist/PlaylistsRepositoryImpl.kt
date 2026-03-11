package com.solyakov.playlist.data.playlist

import com.solyakov.playlist.data.database.AppDatabase
import com.solyakov.playlist.data.database.PlaylistEntity
import com.solyakov.playlist.data.network.Track
import com.solyakov.playlist.domain.repository.PlaylistsRepository
import com.solyakov.playlist.toPlaylist
import com.solyakov.playlist.toTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistsRepositoryImpl(
    database: AppDatabase
): PlaylistsRepository {
    private val playlistsDao = database.PlaylistsDao()
    private val linkDao = database.TableLinkDao()



    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistsDao.getAllPlaylists().map {
            it.map {
                it.toPlaylist()
            }
        }
    }

    override suspend fun addPlaylist(name: String, description: String, image: String?) {
        val playlist = PlaylistEntity(
            name = name,
            description = description,
            image = image
        )
     playlistsDao.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        playlistsDao.deletePlaylist(playlistId)
    }


    override suspend fun getPlaylist(id: Long): Playlist {
        return playlistsDao.getPlaylist(id).toPlaylist()
    }


    override fun getAllTrackInPlaylist(playlistId: Long): Flow<List<Track>> {
       return linkDao.getTracksForPlaylist(playlistId).map {
           it.map {
               it.toTrack()
           }
       }
    }

    override fun getCountTracksInPlaylist(playlistId: Long): Flow<Int> {
        return linkDao.getTracksCount(playlistId)
    }
}